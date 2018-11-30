package com.fernando.zeus.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.zeus.model.Usuario;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ClientWS {

   public static final String AUTHORIZATION = "Basic ZmVybmFuZG86MTIzNA==";

   public static List getListaObjeto(String pathConexao, Class clazz){
      ObjectMapper mapper = new ObjectMapper();
      String mensagem = null;
      HttpURLConnection connection = null;
      try {
         URL url = new URL(pathConexao);
         connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");
         connection.setConnectTimeout(15000);
         connection.setRequestProperty("Authorization", ClientWS.AUTHORIZATION);
         connection.connect();
         String responseJson = ClientWS.inputStreamToString(connection.getInputStream());
         connection.disconnect();
         //List<Usuario> lista = mapper.readValue(responseJson, new TypeReference<List<Usuario>>(){});
         List myObjects = mapper.readValue(responseJson, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
         return myObjects;
      } catch (JsonProcessingException e) {
         e.printStackTrace();
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
      catch (IOException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if(connection != null) {
            connection.disconnect();
         }
      }

      return null;
   }

   public static Object getObjeto(Class tipoObjetoRetorno, String urlWebService, String... parametros) {
      Object objetoRetorno = null;
	
      try {
         for(String parametro: parametros){
            urlWebService = urlWebService + "/" + parametro.replaceAll(" ", "%20");
         }
		
         URL url = new URL(urlWebService);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");
         connection.setConnectTimeout(15000);
         connection.setRequestProperty("login", "teste");
         connection.setRequestProperty("senha", "teste");
         connection.connect();

         String responseJson = inputStreamToString(connection.getInputStream());
         connection.disconnect();
		
         return fromJson(responseJson, tipoObjetoRetorno);

      } catch (Exception e) {
         e.printStackTrace();
      }	
      return objetoRetorno;
   }

   public static Object postObjeto(Object objetoEnvio, Class tipoObjetoRetorno, String urlWebService) {
      Object objetoRetorno = null;
	
      try {
         String requestJson = toJson(objetoEnvio);
		
         URL url = new URL(urlWebService);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("POST");
         connection.setDoOutput(true);
         connection.setUseCaches(false);
	 connection.setConnectTimeout(15000);
         connection.setRequestProperty("login", "teste");
         connection.setRequestProperty("senha", "teste");
         connection.setRequestProperty("Content-Type", "application/json");
         connection.setRequestProperty("Accept", "application/json");
         connection.setRequestProperty("Content-Length", Integer.toString(requestJson.length()));
			
         DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
         stream.write(requestJson.getBytes("UTF-8"));
         stream.flush();
         stream.close();
         connection.connect();

         String responseJson = inputStreamToString(connection.getInputStream());
         connection.disconnect();
         objetoRetorno = fromJson(responseJson, tipoObjetoRetorno);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return objetoRetorno;
   }


   public static String inputStreamToString(InputStream is) throws IOException {
      if (is != null) {
         Writer writer = new StringWriter();

         char[] buffer = new char[1024];
         try {
            Reader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
               writer.write(buffer, 0, n);
            }
         } finally {
            is.close();
         }
         return writer.toString();
      } else {
         return "";
      }
   }

   public static String toJson(Object objeto) throws Exception {
      ObjectMapper mapper = new ObjectMapper();
      StringWriter jsonValue = new StringWriter();
      mapper.writeValue(new PrintWriter(jsonValue), objeto);
      return jsonValue.toString();
   }


   public static Object fromJson(String json, Class clazz) throws Exception {
      ObjectMapper mapper = new ObjectMapper();
      Object obj = mapper.readValue(json, clazz);
      return obj;
   }

}