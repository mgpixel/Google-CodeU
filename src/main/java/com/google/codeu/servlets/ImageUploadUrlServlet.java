package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * Creates upload urls for user page.
 */
@WebServlet("/image-upload-url")
public class ImageUploadUrlServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    String uploadUrl = blobstoreService.createUploadUrl("/messages");
    uploadUrl += " " + blobstoreService.createUploadUrl("/profile");
    response.setContentType("text/html");
    response.getOutputStream().println(uploadUrl);
  }
}