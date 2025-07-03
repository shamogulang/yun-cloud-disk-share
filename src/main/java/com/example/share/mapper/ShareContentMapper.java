package com.example.share.mapper;

import com.example.share.model.ShareContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShareContentMapper {
    int insert(ShareContent shareContent);
    List<ShareContent> selectByShareId(@Param("shareId") Long shareId);
    int deleteByShareId(@Param("shareId") Long shareId);
} 