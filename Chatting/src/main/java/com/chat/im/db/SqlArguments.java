package com.chat.im.db;

import android.content.ContentUris;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

/**
 * sql参数解析
 */

public class SqlArguments {
    public String table;
    public String where;
    public String[] args;
    public int opId;
    public boolean replace;

    public SqlArguments(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 1) {
            this.table = pathSegments.get(0);
            this.replace = false;
        } else if (pathSegments.size() == 2) {
            if ("replace".equals(pathSegments.get(0))) {
                this.table = pathSegments.get(1);
                this.replace = true;
            } else {
                throw new IllegalArgumentException("Invalid URI: " + uri);
            }
        }
        String fragment = uri.getFragment();
        if (!TextUtils.isEmpty(fragment)) {
            try {
                opId = Integer.parseInt(fragment);
            } catch (NumberFormatException e) {
                opId = 0;
            }
        }
    }

    public SqlArguments(Uri uri, String where, String[] args) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 1) {
            this.table = pathSegments.get(0);
            this.where = where;
            this.args = args;
            this.replace = false;
        } else if (pathSegments.size() == 2) {
            if ("replace".equals(pathSegments.get(0))) {
                this.table = pathSegments.get(1);
                this.where = where;
                this.args = args;
                this.replace = true;
            } else if (TextUtils.isEmpty(where)) {
                this.table = pathSegments.get(0);
                try {
                    this.where = "_id=" + ContentUris.parseId(uri);
                } catch (Exception e) {
                    throw new UnsupportedOperationException("WHERE clause not supported: " + uri);
                }
                this.args = null;
                this.replace = false;
            }
        } else if (pathSegments.size() == 3) {
            if ("replace".equals(pathSegments.get(0)) && TextUtils.isEmpty(where)) {
                this.table = pathSegments.get(1);
                try {
                    this.where = "_id=" + ContentUris.parseId(uri);
                } catch (Exception e) {
                    throw new UnsupportedOperationException("WHERE clause not supported: " + uri);
                }
                this.args = null;
                this.replace = true;
            } else {
                throw new UnsupportedOperationException("WHERE clause not supported: " + uri);
            }
        } else {
            throw new UnsupportedOperationException("WHERE clause not supported: " + uri);
        }
        String fragment = uri.getFragment();
        if (!TextUtils.isEmpty(fragment)) {
            try {
                opId = Integer.parseInt(fragment);
            } catch (NumberFormatException e) {
                opId = 0;
            }
        }
    }
}