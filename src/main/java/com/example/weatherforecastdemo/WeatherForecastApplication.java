package com.example.weatherforecastdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class WeatherForecastApplication {

	@Value("${WEATHER_API_KEY}")
	private String weatherApiKey;

	@RequestMapping("/get")
	
	@ResponseBody
	public String getForecast(@RequestParam(name = "city", required = true) String city) {
		return callAndParse("http://api.openweathermap.org/data/2.5/weather?appid=" + weatherApiKey + "&q=" + city).toString();
	}

	private JsonElement callAndParse(String endpoint) {
		URL url;
		try {
			// TODO: extract API key to generalise demo
			url = new URL(endpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
			return new JsonParser().parse(content.toString());
		} catch (IOException e) {
			JsonObject error = new JsonObject();
			error.add("error", new JsonPrimitive(e.getMessage()));
			return error;
		}
	}


	public static void main(String[] args) {
		SpringApplication.run(WeatherForecastApplication.class, args);
	}

}
