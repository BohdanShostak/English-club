package com.inventorsoft.english.googlestorage.client;

public interface GcpFileClient {

  void uploadFile(String bucketName, String fileName, byte[] file);

  byte[] downloadFile(String bucketName, String fileName);

}
