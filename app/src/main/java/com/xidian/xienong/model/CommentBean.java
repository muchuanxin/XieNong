package com.xidian.xienong.model;

import com.xidian.xienong.util.Time;

public class CommentBean implements Comparable<CommentBean>{
	private String comment_id;
	private String worker_id;
	private String worker_name;
	private String farmer_name;
	private String farmer_headPhoto;
	private String comment_date;
	private String work_satisfaction_degree;
	private String service_satisfaction_degree;
	private String comprehensive_value;
	private String comment_content;
	private String serveValue;
	private String input_content;
	
	

	public String getFarmer_headPhoto() {
		return farmer_headPhoto;
	}

	public void setFarmer_headPhoto(String farmer_headPhoto) {
		this.farmer_headPhoto = farmer_headPhoto;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(String worker_id) {
		this.worker_id = worker_id;
	}

	public String getWorker_name() {
		return worker_name;
	}

	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}

	public String getFarmer_name() {
		return farmer_name;
	}

	public void setFarmer_name(String farmer_name) {
		this.farmer_name = farmer_name;
	}

	public String getComment_date() {
		return comment_date;
	}

	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}

	public String getWork_satisfaction_degree() {
		return work_satisfaction_degree;
	}

	public void setWork_satisfaction_degree(String work_satisfaction_degree) {
		this.work_satisfaction_degree = work_satisfaction_degree;
	}

	public String getService_satisfaction_degree() {
		return service_satisfaction_degree;
	}

	public void setService_satisfaction_degree(String service_satisfaction_degree) {
		this.service_satisfaction_degree = service_satisfaction_degree;
	}

	public String getComprehensive_value() {
		return comprehensive_value;
	}

	public void setComprehensive_value(String comprehensive_value) {
		this.comprehensive_value = comprehensive_value;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}


	public String getServeValue() {
		return serveValue;
	}

	public void setServeValue(String serveValue) {
		this.serveValue = serveValue;
	}

	public String getInput_content() {
		return input_content;
	}

	public void setInput_content(String input_content) {
		this.input_content = input_content;
	}

	@Override
	public int compareTo(CommentBean another) {
		// TODO Auto-generated method stub
		if(Time.compare_time(this.comment_date ,another.getComment_date()) == 1){
			return -1;
		}else if(Time.compare_time(this.comment_date ,another.getComment_date()) == -1){
			return 1;
		}else{
			return 0;
		}
	}
	

}
