package Utilities;

public class payloadcreate {
	
	public static String generatePayload(String vendor) {
		
		String postRequest = "{\r\n\"vendorIdList\":[\""+vendor+"\"]\r\n}";
		return postRequest;
		
		
	}

}
