package helloworld;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONArray;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, String> {

    private AmazonDynamoDB amazonDynamoDB;
    private DynamoDB dynamoDB;
    private String tableName = "DHT11";
    private String regions = "ap-northeast-2";

    public String handleRequest(final Object input, final Context context) {
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(regions).build();
        dynamoDB = new DynamoDB(amazonDynamoDB);

        Table table = dynamoDB.getTable(tableName);

        ScanRequest scanRequest = new ScanRequest().withTableName(tableName);
        ScanResult scanResult = amazonDynamoDB.scan(scanRequest);

        JSONArray jsonArray = new JSONArray();

        for(Map<String, AttributeValue> item : scanResult.getItems()) {
            Item scanItem = ItemUtils.toItem(item);
            System.out.println("toJson : " + scanItem.toJSON());
            System.out.println("toJsonPretty" + scanItem.toJSONPretty());
            jsonArray.add(scanItem.toJSON());
        }

        return jsonArray.toString().replaceAll("\\\\", "");
    }
}
