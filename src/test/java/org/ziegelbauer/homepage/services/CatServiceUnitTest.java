package org.ziegelbauer.homepage.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.ziegelbauer.homepage.data.AWSS3Repository;
import org.ziegelbauer.homepage.data.CatPictureRepository;
import org.ziegelbauer.homepage.models.CatPicture;

import java.io.IOException;
import java.io.InvalidObjectException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CatServiceUnitTest {
    @MockBean
    private CatPictureRepository catPictureRepository;

    @MockBean
    private AWSS3Repository awsS3Repository;

    @Autowired
    private CatService catService;

    @Test
    public void addNewThrowsOnInvalidUpload() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                (byte[]) null
        );

        Exception exception = assertThrows(InvalidObjectException.class, () -> {
            catService.addNew(mockMultipartFile);
        });

        assertTrue(exception.getMessage().contains("File upload failed"));
    }

    @Test
    public void addNewProducesValidCatPic() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Dummy Data".getBytes()
        );

        catService.addNew(mockMultipartFile);

        ArgumentCaptor<CatPicture> catCaptor = ArgumentCaptor.forClass(CatPicture.class);

        verify(catPictureRepository).save(catCaptor.capture());

        assertThat(catCaptor.getValue())
                .isNotNull()
                .satisfies(s -> {
                    assertThat(s.getFileName().length()).isEqualTo("e12c0247-ce3b-4ff6-b0dd-6f4278d74011.jpg".length());
                })
                .isInstanceOf(CatPicture.class);
    }
}
