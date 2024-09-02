package com.mococo.delivery.application.dto.ai;

import com.mococo.delivery.application.dto.PageInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AIListResponseDto {
    private List<AIReportResponseDto> aiList;
    private PageInfoDto pageInfo;
}
