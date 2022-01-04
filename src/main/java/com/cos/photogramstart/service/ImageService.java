package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;

	@Transactional(readOnly = true) // 영속성 컨텍스트 변경 감지를 해서, 더티체킹, flush(반영) 을 안한다.
	public Page<Image> imageStory(int principalId, Pageable pageable) {
		Page<Image> images = imageRepository.mStory(principalId, pageable);

		images.forEach((image) -> {
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like) -> {
				if (like.getUser().getId() == principalId) {// 해당 이미지에 좋아요한 사람들을 찾아서 현재 로그인한 사람이 좋아요한 것인지 비교
					image.setLikeState(true);
				}
			});
		});

		return images;
	}

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public void imageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename(); // 1.jpg 이름그대로 들어온다.
		System.out.println("DB에 저장될 파일 이름 : " + imageFileName);

		Path imageFilePath = Paths.get(uploadFolder + imageFileName);

		// 통신, I/O -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
		Image imageEntity = imageRepository.save(image);

	}
	@Transactional(readOnly = true)
	public List<Image> popularImage() {		
		return imageRepository.mPopular();
	}
}
