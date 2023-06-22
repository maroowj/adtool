package com.muzi.adtool.domain;

public class Criteria {
	private int page;
	private int perPageNum;
	private String searchType;
	private String keyword;
	private String order;
	private String desc;
	private String campaign_country;
	private String status;
	private String member_seq;
	private String target;
	private String brand_seq;
	private String point_status;

	//getter setter
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Criteria() {
		this(1, 10);
	}
	public Criteria(int page, int perPageNum) {
		this.page = page;
		this.perPageNum = perPageNum;
	}
	public int getPageStart() {
		return (this.page - 1) * perPageNum;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCampaign_country() {
		return campaign_country;
	}

	public void setCampaign_country(String campaign_country) {
		this.campaign_country = campaign_country;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMember_seq() {
		return member_seq;
	}

	public void setMember_seq(String member_seq) {
		this.member_seq = member_seq;
	}

	public String getTarget() {
		return target;
	}

	public String getBrand_seq() {
		return brand_seq;
	}

	public void setBrand_seq(String brand_seq) {
		this.brand_seq = brand_seq;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getPoint_status() {
		return point_status;
	}

	public void  setPoint_status(String point_status) {
		this.point_status = point_status;
	}
	@Override
	public String toString() {
		return "Criteria{" +
				"page=" + page +
				", perPageNum=" + perPageNum +
				", searchType='" + searchType + '\'' +
				", keyword='" + keyword + '\'' +
				", order='" + order + '\'' +
				", desc='" + desc + '\'' +
				", campaign_country='" + campaign_country + '\'' +
				", status='" + status + '\'' +
				", member_seq='" + member_seq + '\'' +
				", target='" + target + '\'' +
				", brand_seq='" + brand_seq + '\'' +
				'}';
	}
}