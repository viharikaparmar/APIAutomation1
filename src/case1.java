import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import Utilities.payloadcreate;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class case1 {
	
 
	static String hotelId,ratePlanId,roomTypeId;
	 
	@Test(priority=1,enabled=false)
	public void getF1search() {
		
		RestAssured.baseURI="http://192.168.17.114:9006";
		
		Response f1SearchResponseRaw = 
		given().
				param("city","Mumbai").
				param("type","all").
				param("checkInDate", "20/08/2019").
				param("checkOutDate", "21/08/2019").
				param("rooms[0].id","1").
				param("rooms[0].noOfAdults","2").
				header("x-api-key","JN9aWyj7hJ1ZrExC7Ozo").
		when().
				get("/f1/v1/api/pageSearch").
		then().
				assertThat().statusCode(200).
		extract().
				response();
		
		//converting raw response from api to string
		String f1SearchResponse = f1SearchResponseRaw.asString();
		//Printing f1 response
		//System.out.println(f1SearchResponse);
		
		//Converting string to Json
		JsonPath f1SearchResponseJson = new JsonPath(f1SearchResponse);
		
		//Getting first hotel Id from response
		hotelId = f1SearchResponseJson.get("data.content.hotels[0].id");
		System.out.println(hotelId);
		
		//Printing all hotel ids
		int noofhotels = f1SearchResponseJson.getInt("data.content.hotels.size()");
		System.out.println(noofhotels);
		for(int i=0;i<noofhotels;i++) {
			String h1 = f1SearchResponseJson.get("data.content.hotels["+i+"].id");
			System.out.println(h1);
		}
		
	}
	
		@Test(priority=2,enabled=false)
		public void getF1Detail() {

		RestAssured.baseURI="http://192.168.17.114:9006";	
	
		Response f1DetailResponseRaw = given().log().all().
				param("checkInDate", "20/08/2019").
				param("checkOutDate", "21/08/2019").
				header("x-api-key","JN9aWyj7hJ1ZrExC7Ozo").
		when().
				get("/f1/v1/api/hotels/" + hotelId ).
		then().
				assertThat().statusCode(200).
		extract().
				response();		
		
		//converting raw response from api to string
		String f1DetailResponse = f1DetailResponseRaw.asString();
		//Printing f1 response
		//System.out.println(f1DetailResponse);
		
		//Converting string to Json
		JsonPath f1DetailResponseJson = new JsonPath(f1DetailResponse);
		
		//Getting rateplanid and roomtypeid 
		case1.ratePlanId = f1DetailResponseJson.get("data.rates.ratePlanId[0]");
		case1.roomTypeId = f1DetailResponseJson.get("data.rates.roomTypeId[0]");
		
		}
		
		@Test(priority=3,enabled=false)
		public void getF1Review() {
			
			System.out.println(case1.hotelId + "," + case1.ratePlanId + "," + case1.roomTypeId);
	    RestAssured.baseURI="http://192.168.17.114:9006";
		
	    
		Response f1ReviewResponseRaw = given().log().all().
				param("checkInDate", "20/08/2019").
				param("checkOutDate", "21/08/2019").
				param("hotelId",hotelId).
				param("ratePlanId",ratePlanId).
				param("roomTypeId",roomTypeId).
				header("x-api-key","JN9aWyj7hJ1ZrExC7Ozo").
		when().
				get("/f1/v1/api/hotels/rates").
		then().
				assertThat().statusCode(200).
		extract().
				response();		
		
		
		}
		
	@Test(dataProvider="Vendors")
	public void posttest(String v1) {
		
		RestAssured.baseURI="http://192.168.17.115:2010";
		
		given().log().all().
				body(payloadcreate.generatePayload(v1)).
				header("x-api-key", "fdf33438-8d61-4797-bac1-257ea6794a4a").
				header("Content-Type","application/json").
				when().
				post("/vendormargin/v1/contract/create/digilist").
				then().assertThat().statusCode(200);
				
		
	}
	
//	@Test(dataProvider = "Vendors")
//    public void testMethod(String data) {
//        System.out.println("Data is: " + data);
//    }
    
	@DataProvider(name="Vendors")
	public Object[][] getVendors(){
		return new Object[][] {{"00080234"},{"00009581"}};
	}

}
