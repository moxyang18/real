package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Topic;

public interface TopicService {
	// add a new topic
	void addTopic(Topic topic);
	// get all topic lists
	List<Topic> getTopicList();
	// determine whether the topic exists
	boolean containsTopic(String tName);
	// get the list of topics based on input condition
	List<Topic> getTopicList(Topic topic, int limit);
}
