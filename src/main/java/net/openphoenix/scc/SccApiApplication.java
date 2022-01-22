package net.openphoenix.scc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.openphoenix.scc.sensors.Sensor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@RestController
@Controller
public class SccApiApplication {
	static final String DB_URL = "jdbc:mysql://54.38.50.59:3306/www6854_wholesome";
	static final String USER = "www6854_wholesome";
	static final String PASS = "auHE6kYLNmsgBa6fvvG8";
	List<String> toBeInserted = new ArrayList<>();
	public static void main(String[] args) {
		SpringApplication.run(SccApiApplication.class, args);
		System.out.println("dasdas");
	}

	@CrossOrigin
	@GetMapping("/error")
	public String error(HttpServletRequest request) {
		String message = (String) request.getSession().getAttribute("error.message");
		request.getSession().removeAttribute("error.message");
		return message;
	}
	//GROUP CREATION
	@CrossOrigin
	@GetMapping("/sensors/creategroup")
	public String createGroup(@RequestParam(value = "groupName required=false") String groupName){

	try {
		String sqlStatement = "INSERT INTO sensor_groups (group_name,group_UUID) VALUES(\""+groupName+"\",\""+UUID.randomUUID().toString()+"\")";
		System.out.println(sqlStatement);
		String connectionUrl = "jdbc:mysql://54.38.50.59:3306/www6854_wholesome?autoReconnect=true&useSSL=false";
		System.out.println("trying");
		Connection conn = DriverManager.getConnection(connectionUrl, "www6854_wholesome", "auHE6kYLNmsgBa6fvvG8");
		PreparedStatement ps = conn.prepareStatement(sqlStatement);
		ps.execute();
	} catch (SQLException throwables) {
		throwables.printStackTrace();
	}


		return "NOICE ?";
	}
	@CrossOrigin
	@GetMapping("/service/serviceonline")
	public String createGroup(){
		return "ONLINE";
	}


	@CrossOrigin
	@GetMapping("/sensors/getlistofsensors")
	public String getListOfSenors() {
		String sqlStatement = "SELECT `UUID` FROM `registered_UUIDs`";
		System.out.println(sqlStatement);
		String connectionUrl = "jdbc:mysql://54.38.50.59:3306/www6854_wholesome?autoReconnect=true&useSSL=false";
		try (Connection conn = DriverManager.getConnection(connectionUrl, "www6854_wholesome", "auHE6kYLNmsgBa6fvvG8");
			 PreparedStatement ps = conn.prepareStatement(sqlStatement);
			 ResultSet rs = ps.executeQuery()) {
			List<String> sensorData = new ArrayList<String>();
			String ye;
			while (rs.next()) {

				sensorData.add(rs.getString("UUID"));
				// do something with the extracted data...

			}
			ObjectMapper objectMapper = new ObjectMapper();
			//Set pretty printing of json
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			conn.close();
			System.out.println(conn.isClosed());
			return objectMapper.writeValueAsString(sensorData);

		} catch (SQLException e) {
			// handle the exceptionreturn
			System.out.println("heloo");
			e.printStackTrace();
			return "OHNO";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "OHNO";
	}
	//SENSORDATA RETRIVEMENT
	@CrossOrigin
	@GetMapping("/sensors/getsensordata")
	public String getSensorData(@RequestParam(value = "sensorUUID") String sensorUUID, @RequestParam(value = "amount") String amount) {
		try{
			int am = Integer.parseInt(amount);
			UUID uuid = UUID.fromString(sensorUUID);
			String sqlStatement = "SELECT * FROM `"+ sensorUUID +"` ORDER BY `id` DESC LIMIT "+am+"";
			System.out.println(sqlStatement);
			String connectionUrl = "jdbc:mysql://54.38.50.59:3306/www6854_wholesome?autoReconnect=true&useSSL=false";
			System.out.println("trying");
			try (Connection conn = DriverManager.getConnection(connectionUrl, "www6854_wholesome", "auHE6kYLNmsgBa6fvvG8");
				 PreparedStatement ps = conn.prepareStatement(sqlStatement);
				 ResultSet rs = ps.executeQuery()) {
				List<Sensor> sensorData = new ArrayList<Sensor>();
				if(am==1)
				{
					rs.next();
					Sensor sensor = new Sensor(rs.getString("json_data"),sensorUUID ,rs.getString("data_type"),rs.getString("id"),rs.getString("time"));
					//System.out.println(sensor.getJsonValues());
					ObjectMapper objectMapper = new ObjectMapper();
					//Set pretty printing of json
					objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
					conn.close();
					return objectMapper.writeValueAsString(sensor);
				}
				while (rs.next()) {

					sensorData.add(new Sensor(rs.getString("json_data"),sensorUUID ,rs.getString("data_type"),rs.getString("id"),rs.getString("time")));
					// do something with the extracted data...

				}
				ObjectMapper objectMapper = new ObjectMapper();
				//Set pretty printing of json
				objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
				conn.close();
				return objectMapper.writeValueAsString(sensorData);

			} catch (SQLException e) {
				// handle the exceptionreturn
				System.out.println("heloo");
				e.printStackTrace();
				return "OHNO";
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			//do something
		} catch (IllegalArgumentException exception){
			System.out.println("Something is off, I can feel it");
			return "error";
			//9 meaning wrong 3//meaning data //2 meaning type ok stop it
		}
return "OK";
	}
	//SENSORDATA INSERT
	//REMEMBER TO USE %7B AND %7D CODES WHILE USING API FOR PROPER JSON ENCODING
	//
	@CrossOrigin
	@GetMapping("/sensors/insertsensordata")
	public String insertSensorData(@RequestParam(value = "sensorUUID") String sensorUUID,@RequestParam(value = "dataType") String dataType,@RequestParam(value = "jsonData") String jsonData) {
		try{
			String sqlStatement = "INSERT INTO `"+sensorUUID+"`(`json_data`, `data_type`) VALUES ('"+jsonData+"','"+dataType+"')";
			String connectionUrl = "jdbc:mysql://54.38.50.59:3306/www6854_wholesome?autoReconnect=true&useSSL=false";
			System.out.println(sqlStatement);
			System.out.println("trying");
			try{
				 Connection conn = DriverManager.getConnection(connectionUrl, "www6854_wholesome", "auHE6kYLNmsgBa6fvvG8");
				 PreparedStatement ps = conn.prepareStatement(sqlStatement);
				 ps.execute();
				 conn.close();
				 return "success";
			} catch (SQLException e) {
				// handle the exception
				System.out.println("heloo");
				e.printStackTrace();
				return "failure";
			}
			//do something
		} catch (IllegalArgumentException exception){
			System.out.println("Something is off, I can feel it");
			return "error";
			//9 meaning wrong 3//meaning data //2 meaning type ok stop it
		}
	}
	@CrossOrigin
	@PostMapping("/sensors/post/insertsensordata")
	//@GetMapping("/sensors/insertsensordata")
	public String insertSensorDataPost(@RequestParam() String sensorUUID,@RequestParam() String dataType,@RequestParam() String jsonData) {
		try{
			String sqlStatement = "INSERT INTO `"+sensorUUID+"`(`json_data`, `data_type`) VALUES ('"+jsonData+"','"+dataType+"')";
			String connectionUrl = "jdbc:mysql://54.38.50.59:3306/www6854_wholesome?autoReconnect=true&useSSL=false";
			System.out.println(sqlStatement);
			System.out.println("trying");
			try{
				Connection conn = DriverManager.getConnection(connectionUrl, "www6854_wholesome", "auHE6kYLNmsgBa6fvvG8");
				PreparedStatement ps = conn.prepareStatement(sqlStatement);
				ps.execute();
				conn.close();
				return "success";

			} catch (SQLException e) {
				// handle the exception
				System.out.println("heloo");
				e.printStackTrace();
				return "failure";
			}
			//do something
		} catch (IllegalArgumentException exception){
			System.out.println("Something is off, I can feel it");
			return "error";
			//9 meaning wrong 3//meaning data //2 meaning type ok stop it
		}
	}
	//SENSOR REGISTRATION
	@CrossOrigin
	@GetMapping("/sensors/registersensor")
	public String registerSensor(){
		try {
			String sqlStatement = "CREATE TABLE `" +UUID.randomUUID().toString() + "` (json_data TEXT, data_type TEXT, id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP )";
			System.out.println(sqlStatement);
			String connectionUrl = "jdbc:mysql://54.38.50.59:3306/www6854_wholesome?autoReconnect=true&useSSL=false";
			System.out.println("trying");
			Connection conn = DriverManager.getConnection(connectionUrl, "www6854_wholesome", "auHE6kYLNmsgBa6fvvG8");
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ps.execute();
			conn.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return "yup";
	}
	

}
