package eu.dfid.worker.wb.raw;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Container for SODA API response.
 *
 * @author Tomas Mrazek
 */
public final class SODAResponse {
   /**
    * Status of the resposne.
    */
   private SODAResponseStatusType status;
   /**
    * Format of the response content.
    */
   private SODAOutputFormatType format;
   /**
    * Content.
    */
   private String content;
   /**
    * URL.
    */
   private URL url;

   /**
    * Creates new instance form the given response.
    *
    * @param response
    *      API response
    */
   public SODAResponse(final Connection.Response response) {
       setContent(response.body())
       .setFormat(SODAOutputFormatType.getFormatForMimeType(response.contentType()))
       .setStatus(SODAResponseStatusType.getStatusForHttpCode(response.statusCode()))
       .setUrl(response.url());
   }

   /**
    * @return response status.
    */
   public SODAResponseStatusType getStatus() {
       return status;
   }
   /**
    * Sets response status.
    *
    * @param status
    *      response status
    * @return this SODAResponse instance for chaining
    */
   private SODAResponse setStatus(final SODAResponseStatusType status) {
       this.status = status;
       return this;
   }
   /**
    * @return response content format
    */
   public SODAOutputFormatType getFormat() {
       return format;
   }
   /**
    * Sets response content format.
    *
    * @param format
    *      response content format
    * @return this SODAResponse instance for chaining
    */
   private SODAResponse setFormat(final SODAOutputFormatType format) {
       this.format = format;
       return this;
   }
   /**
    * @return response content
    */
   public String getContent() {
       return content;
   }
   /**
    * Sets response content.
    *
    * @param content
    *      response content
    * @return this SODAResponse instance for chaining
    */
   private SODAResponse setContent(final String content) {
       this.content = content;
       return this;
   }
   /**
    * @return true if response status is one of error states.
    */
   public boolean isError() {
       return !(status.equals(SODAResponseStatusType.SUCCESS) || status.equals(SODAResponseStatusType.PROCESSING));
   }
   /**
    * Returns error message parsed from reponse content.
    *
    * @return error message or empty string in case that message field doesn't exist or response isn't an error.
    * @throws IOException
    *      in case that response JSON content parsing fails
    */
   public String getErrorMessage() throws IOException {
       if (isError()) {
           ObjectMapper mapper = new ObjectMapper();
           final JsonNode rootNode = mapper.readTree(getContent());
           return rootNode.get("message").textValue();
       }

       return "";
   }
   /**
    * Checks whether response content is empty.
    *
    * @return decision whether response content is empty
    * @throws IOException
    *      in case that response JSON content parsing fails
    */
   public boolean isEmpty() throws IOException {
       ObjectMapper mapper = new ObjectMapper();
       final JsonNode rootNode = mapper.readTree(getContent());
       return (rootNode == null || rootNode.size() == 0);
   }

   /**
    * Returns request URL for this response.
    *
    * @return URL
    */
    public URL getUrl() {
        return url;
    }

    /**
     * @param url
     *      url to set
     * @return this instance for chaining
     */
    private SODAResponse setUrl(final URL url) {
        this.url = url;
        return this;
    }
}
