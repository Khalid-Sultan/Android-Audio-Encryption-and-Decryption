package aait.se.mobileprogrammingcourse.khalid.android.encryption;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity {
    Button btn_Dec;
    Button btn_In;
    Context ctx;
    byte[] rawKey;
    TextView text;
    private final String KEY = "abc";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        btn_Dec = findViewById(R.id.decryptButton);
        btn_In = findViewById(R.id.encryptButton);
        text = findViewById(R.id.textView2);

        btn_Dec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    decrypt("encrypted.mp3");
                    btn_Dec.setActivated(true);
                    btn_In.setActivated(false);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btn_In.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    encrypt("Hi.mp3");
                    btn_Dec.setActivated(false);
                    btn_In.setActivated(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(KEY.getBytes());
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            rawKey = skey.getEncoded();
        }
        catch (Exception ex){

        }
    }

    public boolean FileExists(String fname) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), fname);
        return file.exists();
    }

    public void encrypt(String filename) throws IOException {
        text.setText("");
        text.append("Encryption Process Started\n");
        byte[] file;
        byte[] result = null;
        if (FileExists(filename)) {
            try {
                File file2 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), filename);
                String path = file2.getAbsolutePath();
                FileInputStream in = new FileInputStream(path);
                if (in != null) {
                    file = new byte[in.available()];
                    int bytes;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    while ((bytes = in.read(file)) != -1) {
                        output.write(file, 0, bytes);
                    }
                    result = output.toByteArray();
                    text.append("File accessed and Processed to a byte array\n");
                }
            } catch (FileNotFoundException ex) {
                Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] encrypted;
            try {
                SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                encrypted = cipher.doFinal(result);
                text.append("Byte array Encrypted\n");
                try {
                    File file3 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "encrypted.mp3");
                    String path = file3.getAbsolutePath();
                    FileOutputStream out = new FileOutputStream(path);
                    out.write(encrypted);
                    out.close();
                    text.append("File is Encrypted\n");
                }
                catch (Exception ex) {
                    Toast.makeText(this, "Error in out stream", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception ex) {
                Toast.makeText(this, "Error in entire stream", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void decrypt(String filename) throws IOException{
        text.setText("");
        text.append("Decryption Process Started\n");
        byte[] file;
        byte[] result = null;
        if (FileExists(filename)) {
            try {
                File file2 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), filename);
                String path = file2.getAbsolutePath();
                FileInputStream in = new FileInputStream(path);
                if (in != null) {
                    file = new byte[in.available()];
                    int bytes;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    while ((bytes = in.read(file)) != -1) {
                        output.write(file, 0, bytes);
                    }
                    result = output.toByteArray();
                    text.append("File accessed and Processed to a byte array\n");
                }
            }
            catch (FileNotFoundException ex) {
                Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            byte[] decrypted;
            try {
                SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                decrypted = cipher.doFinal(result);
                text.append("Byte array Decrypted\n");
                try {
                    File file3 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "decrypted.mp3");
                    String path = file3.getAbsolutePath();
                    FileOutputStream out = new FileOutputStream(path);
                    out.write(decrypted);
                    out.close();
                    text.append("File is Decrypted\n");
                }
                catch (Exception ex) {
                    Toast.makeText(this, "Error in out stream", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception ex) {
                Toast.makeText(this, "Error in entire stream", Toast.LENGTH_SHORT).show();
            }
        }
    }
}