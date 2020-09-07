package com.yosep.msa.yosfrontserver.fileIO;
//package com.yosep.msa.yosfrontend.fileIO;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.Protocol;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.Bucket;
//import com.amazonaws.services.s3.model.CORSRule;
//import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//
//public class S3Util {
//	// bucketName
//	private String bucketName = "yoggaebi-msa-bucket";
//	private String accessKey = ""; // 엑세스 키
//	private String secretKey = ""; // 보안 엑세스 키
//
//	private AmazonS3 conn;
//
//	List<CORSRule.AllowedMethods> rule1AM = new ArrayList<CORSRule.AllowedMethods>();
//	
//	public S3Util() {
////		rule1AM.add(CORSRule.AllowedMethods.PUT);
////		rule1AM.add(CORSRule.AllowedMethods.POST);
////		rule1AM.add(CORSRule.AllowedMethods.DELETE);
////		CORSRule rule1 = new CORSRule().withId("CORSRule1").withAllowedMethods(rule1AM)
////				.withAllowedOrigins(Arrays.asList(""));
//		
//		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//		this.conn = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
//				.withRegion(Regions.AP_NORTHEAST_2).build();
//	}
//
//	// 버킷 리스트를 가져오는 메서드이다.
//	public List<Bucket> getBucketList() {
//		return conn.listBuckets();
//	}
//	
//	// 버킷 가져오기
//	public String getBucketname() {
//		return bucketName;
//	}
//
//	// 버킷을 생성하는 메서드이다.
//	public Bucket createBucket(String bucketName) {
//		return conn.createBucket(bucketName);
//	}
//
//	// 폴더 생성 (폴더는 파일명 뒤에 "/"를 붙여야한다.)
//	public void createFolder(String bucketName, String folderName) {
//		conn.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
//	}
//
//	// 파일 업로드
//	public void fileUpload(String bucketName, String fileName, byte[] fileData) throws FileNotFoundException {
//
//		String filePath = (fileName).replace(File.separatorChar, '/'); // 파일 구별자를 `/`로 설정(\->/) 이게 기존에 / 였어도 넘어오면서 \로
//																		// 바뀌는 거같다.
//		ObjectMetadata metaData = new ObjectMetadata();
//
//		metaData.setContentLength(fileData.length); // 메타데이터 설정 -->원래는 128kB까지 업로드 가능했으나 파일크기만큼 버퍼를 설정시켰다.
//		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData); // 파일 넣음
//
//		conn.putObject(bucketName, filePath, byteArrayInputStream, metaData);
//	}
//
//	// 파일 삭제
//	public void fileDelete(String bucketName, String fileName) {
//		String imgName = (fileName).replace(File.separatorChar, '/');
//		conn.deleteObject(bucketName, imgName);
//		System.out.println("삭제성공");
//	}
//
//	// 파일 URL
//	public String getFileURL(String bucketName, String fileName) {
//		System.out.println("넘어오는 파일명 : " + fileName);
//		String imgName = (fileName).replace(File.separatorChar, '/');
//		return conn.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, imgName)).toString();
//	}
//
//	// 파일 복사
//	public void fileCopy(String from, String to) {
//		try {
//			conn.copyObject(from, accessKey, to, accessKey);
//		}catch(AmazonServiceException e) {
//			e.printStackTrace();
//		}
//	}
//}
