import net.velocloud.swagger.client.VCApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.model.*;
import net.velocloud.swagger.api.*;

import com.sun.jersey.api.client.ClientResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GatewayExample {

  private static final AllApi api = new AllApi();
  private static ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) {

      // EDIT PARAMS AS NEEDED
      String HOSTNAME = "HOSTNAME";
      String USERNAME = "USERNAME";
      String PASSWORD = "PASSWORD";

      GatewayExample.api.setApiClient(new VCApiClient());
      api.getApiClient().setBasePath("https://"+HOSTNAME+"portal/rest");
      AuthObject authorization = new AuthObject();
      authorization.setUsername(USERNAME);
      authorization.setPassword(PASSWORD);
      try {
        api.loginOperatorLogin(authorization);
      } catch (ApiException e) {
        System.out.println("Error in OperatorLogin. Is the VCO Portal running and are your credentials valid?");
        return;
      }

      System.out.println("Provisioning Gateway...");
      GatewayGatewayProvision param = new GatewayGatewayProvision();
      param.setIpAddress("1.2.3.4");

      Object response = null;

      try {
        response = api.gatewayGatewayProvision(param);
      } catch (ApiException e) {
        System.out.println("Error in GatewayProvision.");
        return;
      }
      System.out.format("Response: %s\n", response);

      int gatewayId;
      JsonNode node = mapper.valueToTree(response);
      gatewayId = node.get("id").asInt();
      System.out.format("New Gateway successfully provisioned with id %d\n", gatewayId);

      //Update Gateway Attributes
      System.out.println("Updating Gateway attributes");
      GatewayUpdateGatewayAttributes param2 = new GatewayUpdateGatewayAttributes();
      param2.setGatewayId(gatewayId);
      SiteObject siteParam = new SiteObject();
      siteParam.setCity("San Francisco");
      param2.setSite(siteParam);
      try {
        response = api.gatewayUpdateGatewayAttributes(param2);
      } catch (ApiException e) {
        System.out.println("Error in UpdateGatewayAttributes.");
      }
      System.out.format("Response: %s\n", response);
      System.out.println("Gateway successfully updated with site information.");

      //Delete gateway
      System.out.println("Deleting Gateway");
      GatewayDeleteGateway param3 = new GatewayDeleteGateway();
      param3.setGatewayId(gatewayId);
      try {
        response = api.gatewayDeleteGateway(param3);
      } catch (ApiException e) {
        System.out.println("Error in DeleteGateway.");
      }
      System.out.format("Response: %s\n", response);
      System.out.println("Gateway successfully deleted.");
  }
}
