package com.example.share.mapper;

import com.example.share.model.FileInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FileInfoMapper {
    List<FileInfoDTO> selectByIds(@Param("ids") List<Long> ids);
} 