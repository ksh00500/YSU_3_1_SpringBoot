package com.ysu.codereview.dto;

public class PostDto {
    public String  puid, title, postType, themeLanguage;
    public String  authorId, createdAt, content, thumbnailUrl;
    public int     suggestCount, commentCount;
    public boolean hasThumbnail, isSuggested;
}
