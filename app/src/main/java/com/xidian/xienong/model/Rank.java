package com.xidian.xienong.model;

/**
 * Created by koumiaojuan on 2017/6/30.
 */

public class Rank {
    private String rankId;
    private String rankName;
    private String rankDetail;
    private String imgUrl;
    private String rankRule;

    public String getRankRule() {
        return rankRule;
    }

    public void setRankRule(String rankRule) {
        this.rankRule = rankRule;
    }

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRankDetail() {
        return rankDetail;
    }

    public void setRankDetail(String rankDetail) {
        this.rankDetail = rankDetail;
    }
}
