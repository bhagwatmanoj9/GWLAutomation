package baseClasses;

import java.util.HashMap;
import java.util.Random;

/**
 * Utility Class to Generate Test Data on Runtime.
*/

public class DataGenerator{

	public DataGenerator(){
	}
	
	public static String possibleChars = "abcdefghijklmnopqrstuvwxyz";
    public static char[] possibleCharsArray = possibleChars.toCharArray();
    public static int possibleCharsAvailable = possibleChars.length();
    public static Random rp = new Random();

    public static String getValidUserName(){
    	return "";
    }
    
    public static String generateNumber(int len){
    	int i;
    	if (len == 0){
    		return "0";
    		}
    	StringBuilder numString = new StringBuilder();
    	for (i = 0; i < len; i++){
    		numString.append((int)(getRandomNumber()));
    		}
    	return numString.toString();
       }
    
    public static int getRandomNumber(int len){
    	return rp.nextInt(len);
    }

    public static int getRandomNumber(){
    	return rp.nextInt();
    }
      
    public static int getRandomNumber(int min, int max){
    	int randomNumber = rp.nextInt(min) + max;
    	return randomNumber;
    }
    
    public static String generateString(int num){
    	char[] result = new char[num];
        while (num-- > 0)
        {
            result[num] = possibleCharsArray[rp.nextInt(possibleCharsAvailable)];
        }
        return new String(result);
    }
          
    public static String generateEmail(){
    	StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append(generateString(6)).append(".");
        emailBuilder.append(generateString(3)).append("@gmail.com");
        return emailBuilder.toString();
    }
    
    public static String generateLangCode(){
    	StringBuilder langCode = new StringBuilder();
    	langCode.append(generateString(2)).append(getRandomNumber(3));
        return langCode.toString();
    }
    
    public static String generateInternalDomainEmail(){
    	StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append(generateString(4)).append(".");
        emailBuilder.append(generateString(2)).append("@giggso.com");
        return emailBuilder.toString();
    }
    
    public static String generateInvalidEmail(){
    	StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append(generateString(6)).append("-");
        emailBuilder.append(generateString(3)).append("@@gmail");
        return emailBuilder.toString();
    }

    public static HashMap<String, Object> generateNewLangPayload(){
    	HashMap<String, Object>  jsonPayload = new HashMap<>();
		jsonPayload.put("Name", generateString(6));
		jsonPayload.put("Code", generateLangCode());
		jsonPayload.put("Status", "ACTIVE");
		jsonPayload.put("DataStateFlag", "I");	
    	return jsonPayload;
    }
    
    public static HashMap<String, Object> generateNewIndustryPayload(int industryId){
    	HashMap<String, Object>  jsonPayload = new HashMap<>();
    	jsonPayload.put("Id", industryId);
		jsonPayload.put("Name", generateString(6));
		jsonPayload.put("Description", generateString(8));
		jsonPayload.put("Status", "ACTIVE");
		jsonPayload.put("DataStateFlag", "I");	
    	return jsonPayload;
    }
    
    public static HashMap<String, Object> generateNewSubIndustryPayload(int industryId){
    	HashMap<String, Object>  jsonPayload = new HashMap<>();
    	jsonPayload.put("Name", generateString(8));
    	jsonPayload.put("IndustryId", industryId);
		jsonPayload.put("Status", "Active");
		jsonPayload.put("DataStateFlag", "I");	
    	return jsonPayload;
    }
    
    public static HashMap<String, Object> generateAccountGroupPayload(){
    	HashMap<String, Object>  jsonPayload = new HashMap<>();
    	jsonPayload.put("Name", generateString(8));
    	jsonPayload.put("Language", generateString(6));
    	jsonPayload.put("TimeZone", "-5");
    	jsonPayload.put("ContractEndDate", "2020-01-01T00:00:00");
    	jsonPayload.put("PrimaryContactId", generateString(36));
		jsonPayload.put("Status", "I");
		jsonPayload.put("DataStateFlag", "I");	
    	return jsonPayload;
    }




    




}