package com.example.sadovod;

import java.util.List;

public class VkStructure {
    public VkResponse response;
}

class VkResponse {
    public int count;
    public List<VkPost> items;
}

class VkPost{
    public long id;
    public List<VkAttachment> attachments;
}

class VkAttachment{
    public VkPhoto photo;
}

class VkPhoto{
    public  long id;
    public List<PhotoSizes> sizes;
}

class PhotoSizes{
    public int height;
    public String url;
    public int width;
}