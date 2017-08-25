/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.chat.im.cookie;

import android.text.TextUtils;

import com.chat.im.helper.SpHelper;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class PersistentCookieStore implements CookieStore {
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_NAME_PREFIX = "cookie_";

    private final ConcurrentHashMap<String, Cookie> cookies;
    private final SpHelper cookiePrefs;

    /**
     * Construct a persistent cookie store.
     */
    public PersistentCookieStore() {
        cookiePrefs = SpHelper.getInstance();
        cookies = new ConcurrentHashMap<>();

        // Load any previously stored cookies into the store
        String storedCookieNames = cookiePrefs.get(COOKIE_NAME_STORE, null);
        if (storedCookieNames != null) {
            String[] cookieNames = TextUtils.split(storedCookieNames, ",");
            for (String name : cookieNames) {
                String encodedCookie = cookiePrefs.get(COOKIE_NAME_PREFIX + name, null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null) {
                        cookies.put(name, decodedCookie);
                    }
                }
            }

            // Clear out expired cookies
            clearExpired(new Date());
        }
    }

    @Override
    public void addCookie(Cookie cookie) {
        String name = cookie.getName() + cookie.getDomain();

        // Save cookie into local store, or remove if expired
        if (!cookie.isExpired(new Date())) {
            cookies.put(name, cookie);
        } else {
            cookies.remove(name);
        }

        // Save cookie into persistent store
        cookiePrefs.put(COOKIE_NAME_STORE, TextUtils.join(",", cookies.keySet()));
        cookiePrefs.put(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableCookie(cookie)));
    }

    @Override
    public void clear() {
        // Clear cookies from persistent store
        for (String name : cookies.keySet()) {
            cookiePrefs.remove(COOKIE_NAME_PREFIX + name);
        }
        cookiePrefs.remove(COOKIE_NAME_STORE);

        // Clear cookies from local store
        cookies.clear();
    }

    @Override
    public boolean clearExpired(Date date) {
        boolean clearedAny = false;

        for (ConcurrentHashMap.Entry<String, Cookie> entry : cookies.entrySet()) {
            String name = entry.getKey();
            Cookie cookie = entry.getValue();
            if (cookie.isExpired(date)) {
                // Clear cookies from local store
                cookies.remove(name);

                // Clear cookies from persistent store
                cookiePrefs.remove(COOKIE_NAME_PREFIX + name);

                // We've cleared at least one
                clearedAny = true;
            }
        }

        // Update names in persistent store
        if (clearedAny) {
            cookiePrefs.put(COOKIE_NAME_STORE, TextUtils.join(",", cookies.keySet()));
        }

        return clearedAny;
    }

    @Override
    public List<Cookie> getCookies() {
        return new ArrayList<>(cookies.values());
    }

    protected String encodeCookie(SerializableCookie cookie) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (Exception e) {
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    protected Cookie decodeCookie(String cookieStr) {
        byte[] bytes = hexStringToByteArray(cookieStr);
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            cookie = ((SerializableCookie) ois.readObject()).getCookie();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cookie;
    }

    // Using some super basic byte array <-> hex conversions so we don't have
    // to rely on any large Base64 libraries. Can be overridden if you like!
    protected String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte element : b) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    protected byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}