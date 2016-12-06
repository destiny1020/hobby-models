package com.micropoplar.models.crawl.service.biz;

import com.micropoplar.models.crawl.domain.OneNNNRecordRaw;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * OneNNNRecord比较结果对象。
 * 
 * @author ruixiang
 *
 */
@Data
@AllArgsConstructor
public class OneNNNRecordRawCompareResult {

  private boolean fieldsEqual;
  private boolean imagesCountEqual;
  private OneNNNRecordRaw latestSavedRecord;

  public boolean shoudSave() {
    return !fieldsEqual || !imagesCountEqual;
  }

}
