package com.example.share.mapper;

import com.example.share.model.Share;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShareMapper {
    Share selectById(@Param("id") Long id);
    Share selectByShareUrl(@Param("shareUrl") String shareUrl);
    int insert(Share share);
    int update(Share share);
    List<Share> selectByUserId(@Param("userId") Long userId);
    Share selectByLinkId(@Param("linkId") String linkId);
    int updateStatusByLinkId(@Param("linkId") String linkId, @Param("status") Integer status);
    int countByUserId(@Param("userId") Long userId);
} 