/*
 * Copyright (C) 2017 AICP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.custom.preference;

import android.content.Context;
import android.support.v7.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.provider.Settings;

public class SystemSettingListPreference extends ListPreference {
    private boolean mAutoSummary = false;

    public SystemSettingListPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPreferenceDataStore(new DataStore());
    }

    public SystemSettingListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreferenceDataStore(new DataStore());
    }

    public SystemSettingListPreference(Context context) {
        super(context);
        setPreferenceDataStore(new DataStore());
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        if (mAutoSummary || TextUtils.isEmpty(getSummary())) {
            setSummary(getEntry(), true);
        }
    }

    @Override
    public void setSummary(CharSequence summary) {
        setSummary(summary, false);
    }

    private void setSummary(CharSequence summary, boolean autoSummary) {
        mAutoSummary = autoSummary;
        super.setSummary(summary);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        // This is what default ListPreference implementation is doing without respecting
        // real default value:
        //setValue(restoreValue ? getPersistedString(mValue) : (String) defaultValue);
        // Instead, we better do
        setValue(restoreValue ? getPersistedString((String) defaultValue) : (String) defaultValue);
    }

    protected void putString(String key, String value) {
        Settings.System.putString(getContext().getContentResolver(), key, value);
    }

    protected boolean getString(String key, String defaultValue) {
        return Settings.System.getString(getContext().getContentResolver(),
                key, defaultValue);
    }

    private class DataStore extends PreferenceDataStore {
        @Override
        public void putString(String key, String value) {
            SystemSettingListPreference.this.putString(key, value);
        }
         @Override
        public boolean getString(String key, String defaultValue) {
            return SystemSettingListPreference.this.getString(key, defaultValue);
        }
    }

}
