package com.geartracker.geartracker_backend;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

import java.util.Base64;

import com.google.gson.Gson;

@Path("login")
public class loginResource {

	UserRepository user_repo = UserRepository.getInstance();
	Gson gson = new Gson(); 

	static String encrypt(String strClearText) {
		String strData="";
		try {
			SecretKeySpec skeyspec = new SecretKeySpec(Constants.SECURITY_KEY.getBytes(), Constants.ENCRYPTION_ALG);
			Cipher cipher = Cipher.getInstance( Constants.ENCRYPTION_ALG);
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted = cipher.doFinal(strClearText.getBytes());
			strData = Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}
	
	static String decrypt(String encodedString) {
		String strData="";
		
		try {
			byte[] bytesEncrypted = Base64.getDecoder().decode(encodedString);
			SecretKeySpec skeyspec=new SecretKeySpec(Constants.SECURITY_KEY.getBytes(), Constants.ENCRYPTION_ALG);
			Cipher cipher=Cipher.getInstance(Constants.ENCRYPTION_ALG);
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted=cipher.doFinal(bytesEncrypted);
			strData=new String(decrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	static String getAuthKey(String id, String password) {
		String data = id + "+" + password;
		return encrypt(data);
	}
	
	public static LoginData getLoginCred(String token) {
		String data_string = decrypt(token);
		String[] data = data_string.split("\\+");
		if(data.length!=2) {
			return null;
		}
		LoginData ld = new LoginData();
		ld.setId(data[0]);
		ld.setPassword(data[1]);
		return ld;
	}
	
	@POST
	public AuthUser login(String jsonString) {
		try {
			LoginData l_data = gson.fromJson(jsonString, LoginData.class);
			User u = user_repo.login(l_data.getId(), l_data.getPassword());
			if(u == null) {
				return null;
			}
			String token = getAuthKey(l_data.getId(), l_data.getPassword());
			AuthUser au = new AuthUser(u, token);
			return au;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

}
