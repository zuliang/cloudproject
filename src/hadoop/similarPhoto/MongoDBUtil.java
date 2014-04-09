package hadoop.similarPhoto;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public final class MongoDBUtil {  
	  
    private static final String HOST = "127.0.0.1";  
  
    private static final String dbName = "photo";  
  
    private static Mongo mongo;  
  
    private static DB db;  
    
    private static DBCollection fingerprints;
  
    static {  
        try {  
            mongo = new Mongo(HOST);  
            db = mongo.getDB(dbName);  
            fingerprints = db.getCollection("fingerprint");
            // db.authenticate(username, passwd)  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (MongoException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private MongoDBUtil() {  
    }  
      
    public static boolean collectionExists(String collectionName) {  
        return db.collectionExists(collectionName);  
    }  
      
    public static void dropCollection(String collectionName) {
    	getCollection(collectionName).drop();
	}
    
    public static void insertFingerprint(String photo, String fingerprint) {
		BasicDBObject photoFp = new BasicDBObject("photo", photo).append("fingerprint", fingerprint);
		fingerprints.insert(photoFp);
	}
    
    public static ArrayList<String> findTop3SimilarPhoto(String sourcePath, String collectionName) {
    	Pattern pattern = Pattern.compile("^.*" + sourcePath+ ".*$", Pattern.CASE_INSENSITIVE); 
    	DBCursor dbCursor = getCollection(collectionName).find(new BasicDBObject("_id",pattern)).limit(3).sort(new BasicDBObject("value",-1));
    	ArrayList<String> result = new ArrayList<String>();
    	while (dbCursor.hasNext()) {
    		result.add(dbCursor.next().get("_id").toString().split("\\^\\&\\^")[1]);
    	}
    	return result;
    }
    
    public static DBCollection getCollection(String collectionName) {  
        return db.getCollection(collectionName);  
    }
    
    public static void main(String[] args) {
//    	dropCollection("out");
//		findTop3SimilarPhoto("/home/hduser/workspace/images/source.jpg", "out");
	}
}