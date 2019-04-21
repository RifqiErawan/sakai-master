/**
 * Copyright (c) 2007-2014 The Apereo Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://opensource.org/licenses/ecl2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
* Licensed to The Apereo Foundation under one or more contributor license
* agreements. See the NOTICE file distributed with this work for
* additional information regarding copyright ownership.
*
* The Apereo Foundation licenses this file to you under the Educational 
* Community License, Version 2.0 (the "License"); you may not use this file 
* except in compliance with the License. You may obtain a copy of the 
* License at:
*
* http://opensource.org/licenses/ecl2.txt
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.sakaiproject.signup.restful;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.signup.logic.Permission;
import org.sakaiproject.signup.model.MeetingTypes;
import org.sakaiproject.signup.model.SignupAttachment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * This class holds the information of sign-up meeting/event for RESTful and
 * it's a wrapper class for SignupMeeting.
 * </p>
 * 
 * @author Peter Liu
 */
public class SignupEvent implements Entity, MeetingTypes {

	public static final String USER_SIGNUP = "signup";

	public static final String USER_CANCEL_SIGNUP = "cancelSignup";

	public static final String USER_ADD_TO_WAITLIST = "addToWaitList";

	public static final String USER_REMOVE_FROM_WAITLIST = "removeWaitList";

	/* corresponding field name in Json */
	public static final String USER_ACTION_TYPE_FIELD_NAME = "userActionType";
	/* corresponding field name in Json */
	public static final String ALLOCATE_TO_TS_ID_FIELD_NAME = "allocToTSid";
	/* corresponding field name in Json */
	public static final String SITE_ID_FIELD_NAME = "siteId";

	private static final long serialVersionUID = 2L;

	public static final String[] USER_ATION_Types = { USER_SIGNUP,
			USER_CANCEL_SIGNUP, USER_ADD_TO_WAITLIST, USER_REMOVE_FROM_WAITLIST };

	private Long id;

	private Long recurrenceId;

	private String title;

	private String description;

	private String location;

	private String organizerName;

	private Date startTime;

	private Date endTime;
	
	private Date myStartTime;
	
	private Date myEndTime;

	private Date signupBegins;

	private Date signupDeadline;

	private String meetingType;
	
	private String repeatType;
	
	private boolean allowWaitList;
	
	private boolean allowComment;
	
	private boolean eidInputMode;
	
	private boolean autoReminder;

	private List<SignupTimeslotItem> signupTimeSlotItems;

	private List<SignupSiteItem> signupSiteItems;
	
	private List<SignupAttachment> signupMainEventAttachItems;

	private Permission permission;

	private String entityID;

	/* keep current user navigation siteId */
	private String siteId;

	private boolean currentUserSignedUp;

	private String allocToTSid;

	private String availableStatus = null;

	private String userActionType;
	
	private boolean allowAttendance;
	
	/*max # of slots, which user is allowed to sign-up*/
	private Integer maxNumOfSlots;

	public String getId() {
		if (entityID == null) {
			entityID = id + "";
		}
		return entityID;
	}

	public void setId(String s) {
		entityID = s;
	}

	/**
	 * get the unique meeting/event Id, which is generated by DB
	 * 
	 * @return an unique sequence Id
	 */
	public Long getEventId() {
		return id;
	}

	/**
	 * this is a setter.
	 * 
	 * @param id
	 *            an unique sequence Id
	 */
	public void setEventId(Long id) {
		this.id = id;
	}

	/**
	 * get the name of the event/meeting
	 * 
	 * @return the name of the event/meeting
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * this is a setter
	 * 
	 * @param title
	 *            a name of the event/meeting
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrganizerName() {
		return organizerName;
	}

	public void setOrganizerName(String organizerName) {
		this.organizerName = organizerName;
	}

	/**
	 * get the location, where the event/meeting occurs
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * this is a setter.
	 * 
	 * @param location
	 *            a location, where the event/meeting occurs
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * get the description of the event/meeting
	 * 
	 * @return a description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * this is a setter.
	 * 
	 * @param description
	 *            a description of an event/meeting
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * get the end time of an event/meeting
	 * 
	 * @return an end time of the event/meeting
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * this is a setter.
	 * 
	 * @param endTime
	 *            the end time of the event/meeting
	 */
	public void setEndTime(Date endTime) {
		this.endTime = truncateSeconds(endTime);
	}

	public String getAvailableStatus() {
		return availableStatus;
	}

	public void setAvailableStatus(String availableStatus) {
		this.availableStatus = availableStatus;
	}

	/**
	 * get the event/meeting type (individual,group and announcement)
	 * 
	 * @return one of the type (individual,group and announcement)
	 */
	public String getMeetingType() {
		return meetingType;
	}

	/**
	 * this is a setter
	 * 
	 * @param meetingType
	 *            a defined meeting type (individual,group and announcement)
	 */
	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	/**
	 * This method obtains the number of time slots in the event/meeting
	 * 
	 * @return the number of time slots
	 */
	public int getNoOfTimeSlots() {
		return (signupTimeSlotItems == null) ? 0 : signupTimeSlotItems.size();
	}

	/**
	 * get the recurrence Id of the event/meeting
	 * 
	 * @return the recurrence Id
	 */
	public Long getRecurrenceId() {
		return recurrenceId;
	}

	/**
	 * this is a setter.
	 * 
	 * @param recurrenceId
	 *            a unique Id
	 */
	public void setRecurrenceId(Long recurrenceId) {
		this.recurrenceId = recurrenceId;
	}

	/**
	 * get the starting time of the event/meeting
	 * 
	 * @return the starting date of the event/meeting
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * this is a setter
	 * 
	 * @param startTime
	 *            the time when the event/meeting starts
	 */
	public void setStartTime(Date startTime) {
		this.startTime = truncateSeconds(startTime);
	}

	/**
	 * This is a extra field for JSON object to display the event date. It's the
	 * same as startTime and it will make nested JSON object processing much
	 * easier.
	 * 
	 * @return a Date object
	 */
	public Date getDate() {
		return getStartTime();
	}

	/**
	 * get the signup starting time for the event/meeting
	 * 
	 * @return a starting time for signup
	 */
	public Date getSignupBegins() {
		return signupBegins;
	}

	/**
	 * this is a setter
	 * 
	 * @param signupBegins
	 *            a time when the signup process starts
	 */
	public void setSignupBegins(Date signupBegins) {
		this.signupBegins = truncateSeconds(signupBegins);
	}

	/**
	 * get the time when the signup process stops
	 * 
	 * @return a date of the signup deadline
	 */
	public Date getSignupDeadline() {
		return signupDeadline;
	}

	/**
	 * this is a setter
	 * 
	 * @param signupDeadLine
	 *            the time when signup process stops
	 */
	public void setSignupDeadline(Date signupDeadLine) {
		this.signupDeadline = truncateSeconds(signupDeadLine);
	}
	
	public boolean isAllowWaitList() {
		return allowWaitList;
	}

	public void setAllowWaitList(boolean allowWaitList) {
		this.allowWaitList = allowWaitList;
	}

	public boolean isAllowComment() {
		return allowComment;
	}

	public void setAllowComment(boolean allowComment) {
		this.allowComment = allowComment;
	}
	
	public String getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}
	
	public boolean isMySignupEvents(){
		if(myStartTime !=null && myEndTime !=null)
			return true;
		else
			return false;
	}

	public Date getMyStartTime() {
		return myStartTime;
	}

	public void setMyStartTime(Date myStartTime) {
		this.myStartTime = myStartTime;
	}

	public Date getMyEndTime() {
		return myEndTime;
	}

	public void setMyEndTime(Date myEndTime) {
		this.myEndTime = myEndTime;
	}
	
	/**
	 * check if the meeting/event can take attendance
	 * 
	 * @return true if the meeting/event can take attendance
	 */
	public boolean isallowAttendance() {
		return allowAttendance;
	}

	/**
	 * this is a setter.
	 * 
	 * @param allowAttendance
	 *            a boolean value
	 */
	public void setAllowAttendance(boolean allowAttendance) {
		this.allowAttendance = allowAttendance;
	}

	/**
	 * get the maximum nubmer of the attendees, which is allowed in one time
	 * slot
	 * 
	 * @return the maximum nubmer of the attendees
	 */
	public int getMaxNumberOfAttendees() {
		if (signupTimeSlotItems == null || signupTimeSlotItems.isEmpty())
			return 0;

		return signupTimeSlotItems.get(0).getMaxNoOfAttendees();
	}

	/**
	 * get a list of SignupSiteItem objects, which indicate the scope of the
	 * event/meeting
	 * 
	 * @return a list of SignupSiteItem objects,
	 */
	public List<SignupSiteItem> getSignupSiteItems() {
		return signupSiteItems;
	}

	/**
	 * this is a setter
	 * 
	 * @param SignupSiteItem
	 *            a SignupSiteItem object
	 */
	public void setSignupSiteItems(List<SignupSiteItem> signupSites) {
		this.signupSiteItems = signupSites;
	}

	/**
	 * This will test if the event/meeting is started to sign up
	 * 
	 * @return true if the sign-up begin time is before current time.
	 */
	public boolean isStartToSignUp() {
		return signupBegins.before(new Date());
	}

	/**
	 * This method will check if the event/meeting is already expired
	 * 
	 * @return true if the event/meeting is expired
	 */
	public boolean isMeetingExpired() {
		Date today = new Date();
		// pastMeeting => today>endDate => value>0
		int value = today.compareTo(endTime);
		return value > 0;
	}

	/**
	 * This method will check if the current time has already passed the signup
	 * deadline
	 * 
	 * @return true if the current time has already passed the signup deadline
	 */
	public boolean isPassedDeadline() {
		Date today = new Date();
		int value = today.compareTo(signupDeadline);
		return value > 0;
	}

	private Calendar cal = Calendar.getInstance();

	/**
	 * This will test if the event/meeting is cross days
	 * 
	 * @return true if the event/meeting is cross days
	 */
	public boolean isMeetingCrossDays() {
		cal.setTime(getStartTime());
		int startingDay = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTime(getEndTime());
		int endingDay = cal.get(Calendar.DAY_OF_YEAR);
		return (startingDay != endingDay);
	}

	public boolean isCurrentUserSignedUp() {
		return currentUserSignedUp;
	}

	public void setCurrentUserSignedUp(boolean currentUserSignedUp) {
		this.currentUserSignedUp = currentUserSignedUp;
	}

	private String[] allowedUserActionTypes;

	public String[] getAllowedUserActionTypes() {
		return this.allowedUserActionTypes;
	}

	public void setAllowedUserActionTypes(String[] types) {
		this.allowedUserActionTypes = types;
	}

	/**
	 * get a list of SignupTimeslot objects, which contains the event/meeting
	 * segments information
	 * 
	 * @return a list of SignupTimeslot objects
	 */
	public List<SignupTimeslotItem> getSignupTimeSlotItems() {
		return signupTimeSlotItems;
	}

	/**
	 * this is a setter.
	 * 
	 * @param signupTimeSlots
	 *            a list of SignupTimeslot objects
	 */
	public void setSignupTimeSlotItems(
			List<SignupTimeslotItem> signupTimeSlotItems) {
		this.signupTimeSlotItems = signupTimeSlotItems;
	}
	
	
	/**
	 * This method provides the attachments for main Sign-up event page 
	 * and is public to whole site.
	 * @return	a list of SignupAttachment objects.
	 */
	public List<SignupAttachment> getSignupMainEventAttachItems() {
		return signupMainEventAttachItems;
	}

	/**
	 * this is a setter. it only holds the attachments,
	 *  which belongs to the entire event, not to individual 
	 *  time slot or participant
	 * 
	 * @param SignupAttachment
	 *            a list of SignupAttachment objects
	 */
	public void setSignupMainEventAttachItems(
			List<SignupAttachment> signupMainEventAttachItems) {
		this.signupMainEventAttachItems = signupMainEventAttachItems;
	}

	/**
	 * get the Permission object, which contains the user's operation
	 * permissions on this event/meeting
	 * 
	 * @return a Permission object
	 */
	public Permission getPermission() {
		return permission;
	}

	/**
	 * this is a setter
	 * 
	 * @param permission
	 *            a permission object
	 */
	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public String getAllocToTSid() {
		return allocToTSid;
	}

	public void setAllocToTSid(String allocToTSid) {
		this.allocToTSid = allocToTSid;
	}

	/* 'signup', 'cancelSignup','addToWaitList', 'removeWaitList' */
	public String getUserActionType() {
		return userActionType;
	}

	public void setUserActionType(String userActionType) {
		this.userActionType = userActionType;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	public boolean isEidInputMode() {
		return eidInputMode;
	}

	public void setEidInputMode(boolean eidInputMode) {
		this.eidInputMode = eidInputMode;
	}

	public boolean isAutoReminder() {
		return autoReminder;
	}

	public void setAutoReminder(boolean autoReminder) {
		this.autoReminder = autoReminder;
	}

	/**
	 * Set the second value to zero. it only need to accurate to minutes level.
	 * Otherwise it may cause one minute shorter display confusion
	 * 
	 * @param time
	 *            a Date object
	 * @return a Date object
	 */
	private Date truncateSeconds(Date time) {
		/* set second to zero */
		if (time == null)
			return null;

		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public ResourceProperties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReference() {
		return ServerConfigurationService.getAccessUrl() + "/signupEvent/"
				+ Entity.SEPARATOR + this.getId();
	}

	public String getReference(String arg0) {
		return getReference();
	}
	
	public String getServerUrl(){
		return ServerConfigurationService.getServerUrl();
	}

	public String getUrl() {
		return ServerConfigurationService.getAccessUrl() + "/signupEvent/"
				+ this.getId();
	}

	public String getUrl(String arg0) {
		return getUrl();
	}

	public Element toXml(Document doc, Stack stack) {
		Element event = doc.createElement("signupEvent");

		if (stack.isEmpty()) {
			doc.appendChild(event);
		} else {
			((Element) stack.peek()).appendChild(event);
		}

		stack.push(event);

		event.setAttribute("id", getId());
		event.setAttribute("eventid", getEventId().toString());
		event.setAttribute("title", getTitle());
		event.setAttribute("organizer", this.getOrganizerName());
		event.setAttribute("location", this.getLocation());
		event.setAttribute("event-type", this.getMeetingType());
		event.setAttribute("event-recurrenceId", this.getRecurrenceId()
				.toString());

		if (description != null)
			event.setAttribute("description", description);

		event.setAttribute("start-time", this.getStartTime().toString());
		event.setAttribute("end-time", this.getEndTime().toString());
		event.setAttribute("signup-begin", this.getSignupBegins().toString());
		event.setAttribute("signup-deadline", this.getSignupDeadline()
				.toString());
		// event.setAttribute("repeat-event",
		// Boolean.valueOf(isRecurredMeeting()).toString());
		// properties
		// getProperties().toXml(doc, stack);
		// apppend the options as chidren

		stack.pop();

		return event;
	}

	public String toString() {
		return new ToStringBuilder(this).append(this.id).append(
				this.organizerName).append(this.title).append(this.location)
				.append(this.startTime).append(this.endTime).append(
						this.description).toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SignupEvent other = (SignupEvent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private String userActionWarningMsg;

	public String getUserActionWarningMsg() {
		String msg = this.userActionWarningMsg;
		/* only show once for user */
		this.userActionWarningMsg = null;
		return msg;
	}

	public void setUserActionWarningMsg(String userActionWarningMsg) {
		this.userActionWarningMsg = userActionWarningMsg;
	}

	public Integer getMaxNumOfSlots() {
		return maxNumOfSlots;
	}

	public void setMaxNumOfSlots(Integer maxNumOfSlots) {
		this.maxNumOfSlots = maxNumOfSlots;
	}

}