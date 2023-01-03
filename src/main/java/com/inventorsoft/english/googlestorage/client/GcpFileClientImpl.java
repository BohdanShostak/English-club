package com.inventorsoft.english.googlestorage.client;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GcpFileClientImpl implements GcpFileClient {

    private final Storage storage;

    @SneakyThrows
    @Override
    public void uploadFile(String bucketName, String fileName, byte[] file) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.createFrom(blobInfo, new ByteArrayInputStream(file));
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) {
        return storage.readAllBytes(bucketName, fileName);
    }

    public boolean deleteFile(String bucketName, String fileName) {
        return storage.delete(bucketName, fileName);
    }

    public List<Boolean> deleteListFiles(String bucketName, List<String> fileNames) {
        List<BlobId> blobIds = new LinkedList<>();
        fileNames.forEach(fileName -> blobIds.add(BlobId.of(bucketName, fileName)));
        return storage.delete(blobIds);
    }

}
