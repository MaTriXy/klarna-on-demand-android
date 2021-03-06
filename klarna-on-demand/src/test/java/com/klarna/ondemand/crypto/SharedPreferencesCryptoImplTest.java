package com.klarna.ondemand.crypto;

import android.content.Context;
import android.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(SharedPreferencesCryptoImpl.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "org.json.*" })
public class SharedPreferencesCryptoImplTest extends CryptoTestBase {

    @Override
    protected int getTestSdkVersion() {
        return 17;
    }

    @Override
    protected Crypto getTestSubject(Context context) throws Exception {
        return new SharedPreferencesCryptoImpl(context);
    }

    @Test
    public void sign_shouldCaclculateAValidSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        SharedPreferencesCryptoImpl spyCrypto = (SharedPreferencesCryptoImpl) Mockito.spy(crypto);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode("MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAskcFcO+vuYAQfRjCGkhZXMGlrtpbJZRq9y0SJi2DlKo2Ph58Ni9j6mN4DVKdSSJhuR/myNzXmszLzZIgq1AwpQIDAQABAkA641rYw1O4YqUPrW3wYJWkHhMsftQ8xZnPrAOiuMYOBN5jJ1pKBXiy3nHxBCiSWAJY+kWSWzbY1zzecBtmbCDBAiEA1lrU0Uhk3H7v7qyZPWIgu2pHYi+/W17c3jsl/NgHQfMCIQDU6dea1HCfWcU31cW/wShhzXd4h1H6AqEmRIEGnCLRBwIhAINNAC9x+NZXqwC4GOXQxdwHLdKnDMAbS4+VC5/ldAyhAiEAs4/PhMWbgdisyi0gzFpz2x/0nRLK4SXskKB/jHqLpmsCICTcahC8Xm8EFD/kxkDpI2oMZuWa2ghvZ1zuSMpnfkgE", Base64.DEFAULT));
        KeyFactory keyFact = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFact.generatePrivate(pkcs8EncodedKeySpec);

        Mockito.doReturn(privateKey).when(spyCrypto).getPrivateKey();
        assertThat(spyCrypto.sign("my_data")).isEqualTo("n8PEnZ5s5Cn39Jx2nMRf+cTbw7YIw5ak0HBEQJUl4mX/rMjo1SQe/56elt95c5H9TydbEcmvDeDt\n2SpHXylNDA==\n");
    }

}
