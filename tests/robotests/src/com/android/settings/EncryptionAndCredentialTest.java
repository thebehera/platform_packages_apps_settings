/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.settings;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.testutils.SettingsRobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@RunWith(SettingsRobolectricTestRunner.class)
@Config(manifest = TestConfig.MANIFEST_PATH, sdk = TestConfig.SDK_VERSION)
public class EncryptionAndCredentialTest {

    @Test
    public void getMetricsCategory_shouldReturnEncryptionAndCredential() {
        EncryptionAndCredential fragment = new EncryptionAndCredential();
        assertThat(fragment.getMetricsCategory()).isEqualTo(MetricsEvent.ENCRYPTION_AND_CREDENTIAL);
    }

}
