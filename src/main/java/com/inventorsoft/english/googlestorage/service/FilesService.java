package com.inventorsoft.english.googlestorage.service;

import com.inventorsoft.english.googlestorage.client.GcpFileClientImpl;
import com.inventorsoft.english.googlestorage.model.FileType;
import com.inventorsoft.english.googlestorage.model.Properties;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final GcpFileClientImpl gcpFileClient;

    private final Properties properties;

    Map<FileType, String> map;

    @PostConstruct
    public void initMap() {
        map = Map.of(FileType.HOMEWORK, properties.getHomeWork(),
                FileType.IMAGE, properties.getImages());
    }

    public void uploadFile(String fileName, byte[] file, FileType fileType) {
        String bucketName = map.get(fileType);
        gcpFileClient.uploadFile(bucketName, fileName, file);
    }

    public byte[] downloadFile(String fileName, FileType fileType) {
        String bucketName = map.get(fileType);
        return gcpFileClient.downloadFile(bucketName, fileName);
    }

    public boolean deleteFile(String fileName, FileType fileType) {
        String bucketName = map.get(fileType);
        return gcpFileClient.deleteFile(bucketName, fileName);
    }

    public List<Boolean> deleteListFiles(List<String> fileNames, FileType fileType) {
        String bucketName = map.get(fileType);
        return gcpFileClient.deleteListFiles(bucketName, fileNames);
    }

}
