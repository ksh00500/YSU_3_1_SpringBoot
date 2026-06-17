package com.ysu.codereview.dto;

import java.util.List;
import java.util.Map;

public class JobDto {
    public String                   puid, title, company, location;
    public String                   jobType, companyLogoUrl, companyInitial;
    public List<Map<String,String>> tags;
}
