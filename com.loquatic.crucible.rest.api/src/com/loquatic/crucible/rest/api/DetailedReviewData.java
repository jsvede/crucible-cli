//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.15 at 10:57:54 PM MDT 
//


package com.loquatic.crucible.rest.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for detailedReviewData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="detailedReviewData">
 *   &lt;complexContent>
 *     &lt;extension base="{}reviewData">
 *       &lt;sequence>
 *         &lt;element name="stats" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="comments" type="{}commentStats" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}actions" minOccurs="0"/>
 *         &lt;element name="generalComments" type="{}comments" minOccurs="0"/>
 *         &lt;element ref="{}reviewItems" minOccurs="0"/>
 *         &lt;element ref="{}reviewers" minOccurs="0"/>
 *         &lt;element ref="{}transitions" minOccurs="0"/>
 *         &lt;element name="versionedComments" type="{}comments" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detailedReviewData", propOrder = {
    "stats",
    "actions",
    "generalComments",
    "reviewItems",
    "reviewers",
    "transitions",
    "versionedComments"
})
public class DetailedReviewData
    extends ReviewData
{

    protected DetailedReviewData.Stats stats;
    protected Actions actions;
    protected Comments generalComments;
    protected ReviewItems reviewItems;
    protected Reviewers reviewers;
    protected Transitions transitions;
    protected Comments versionedComments;

    /**
     * Gets the value of the stats property.
     * 
     * @return
     *     possible object is
     *     {@link DetailedReviewData.Stats }
     *     
     */
    public DetailedReviewData.Stats getStats() {
        return stats;
    }

    /**
     * Sets the value of the stats property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailedReviewData.Stats }
     *     
     */
    public void setStats(DetailedReviewData.Stats value) {
        this.stats = value;
    }

    /**
     * Gets the value of the actions property.
     * 
     * @return
     *     possible object is
     *     {@link Actions }
     *     
     */
    public Actions getActions() {
        return actions;
    }

    /**
     * Sets the value of the actions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Actions }
     *     
     */
    public void setActions(Actions value) {
        this.actions = value;
    }

    /**
     * Gets the value of the generalComments property.
     * 
     * @return
     *     possible object is
     *     {@link Comments }
     *     
     */
    public Comments getGeneralComments() {
        return generalComments;
    }

    /**
     * Sets the value of the generalComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comments }
     *     
     */
    public void setGeneralComments(Comments value) {
        this.generalComments = value;
    }

    /**
     * Gets the value of the reviewItems property.
     * 
     * @return
     *     possible object is
     *     {@link ReviewItems }
     *     
     */
    public ReviewItems getReviewItems() {
        return reviewItems;
    }

    /**
     * Sets the value of the reviewItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReviewItems }
     *     
     */
    public void setReviewItems(ReviewItems value) {
        this.reviewItems = value;
    }

    /**
     * Gets the value of the reviewers property.
     * 
     * @return
     *     possible object is
     *     {@link Reviewers }
     *     
     */
    public Reviewers getReviewers() {
        return reviewers;
    }

    /**
     * Sets the value of the reviewers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reviewers }
     *     
     */
    public void setReviewers(Reviewers value) {
        this.reviewers = value;
    }

    /**
     * Gets the value of the transitions property.
     * 
     * @return
     *     possible object is
     *     {@link Transitions }
     *     
     */
    public Transitions getTransitions() {
        return transitions;
    }

    /**
     * Sets the value of the transitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transitions }
     *     
     */
    public void setTransitions(Transitions value) {
        this.transitions = value;
    }

    /**
     * Gets the value of the versionedComments property.
     * 
     * @return
     *     possible object is
     *     {@link Comments }
     *     
     */
    public Comments getVersionedComments() {
        return versionedComments;
    }

    /**
     * Sets the value of the versionedComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comments }
     *     
     */
    public void setVersionedComments(Comments value) {
        this.versionedComments = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="comments" type="{}commentStats" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "comments"
    })
    public static class Stats {

        protected List<CommentStats> comments;

        /**
         * Gets the value of the comments property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the comments property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getComments().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CommentStats }
         * 
         * 
         */
        public List<CommentStats> getComments() {
            if (comments == null) {
                comments = new ArrayList<CommentStats>();
            }
            return this.comments;
        }

    }

}
