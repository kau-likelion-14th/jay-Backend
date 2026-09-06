package likelion14th.lte.youtube.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class YoutubeSongItemResponse {
    private String songId;

    private String title;

    private String artist;

    private String imageUrl;
}
