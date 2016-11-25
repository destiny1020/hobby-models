package com.micropoplar.models.crawl.spec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;

/**
 * 1999列表项目单元测试类。
 * 
 * @author ruixiang
 *
 */
public class OneNNNRecordListRawSpec {

  private OneNNNRecordListRaw record1;
  private OneNNNRecordListRaw record2;
  private OneNNNRecordListRaw record3;

  @Before
  public void before() {
    record1 = new OneNNNRecordListRaw();
    record1.setId(1L);
    record1.setCoverUrl("http://www.1999.co.jp/itsmall42/10428884s.jpg");
    record1.setTitle("日本陸軍四式戦闘機 疾風 & くろがね四起 情景セット (プラモデル)");
    record1.setSn("10428884");
    record1.setMakers("タミヤ");
    record1.setRelease("12月下旬(2016/11/9予約開始)");
    record1.setScales("1/48");
    record1.setSeries("1/48 傑作機、No.116");
    record1.setCode("61116");

    record2 = new OneNNNRecordListRaw();
    record2.setId(2L);
    record2.setCoverUrl("http://www.1999.co.jp/itsmall42/10428884s.jpg");
    record2.setTitle("日本陸軍四式戦闘機 疾風 & くろがね四起 情景セット (プラモデル)");
    record2.setSn("10428884");
    record2.setMakers("タミヤ");
    record2.setRelease("12月下旬(2016/11/9予約開始)");
    record2.setScales("1/48");
    record2.setSeries("1/48 傑作機、No.116");
    record2.setCode("61116");

    record3 = new OneNNNRecordListRaw();
    record3.setId(3L);
    record3.setCoverUrl("http://www.1999.co.jp/itsmall26/10265219s.jpg");
    record3.setTitle("ロッキード マーチン F-16CJ [ブロック50] ファイティングファルコン (プラモデル)");
    record3.setSn("10265219");
    record3.setMakers("タミヤ");
    record3.setRelease("2014年4月上旬");
    record3.setScales("1/72");
    record3.setSeries("ウォーバード、No.86");
    record3.setCode("60786");
  }

  @Test
  public void whenTwoRecordsAreSameThenEquals() {
    assertThat(record1.equals(record2), is(Boolean.TRUE));
  }

  @Test
  public void whenTwoRecordsAreDiffThenNotEquals() {
    assertThat(record1.equals(record3), is(Boolean.FALSE));
  }

}
