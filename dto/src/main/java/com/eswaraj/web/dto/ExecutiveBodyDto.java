package com.eswaraj.web.dto;



/**
 * Represents an executive body like Water Department Office or Fire Department Office at any level
 * @author ravi
 * @date Jun 18, 2014
 *
 */

public class ExecutiveBodyDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	private String name;
	private AddressDto addressDto;
	private Long boundaryId;
    private Long categoryId;
    private Long parentExecutiveBodyId;
	private boolean root;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AddressDto getAddressDto() {
		return addressDto;
	}
	public void setAddressDto(AddressDto addressDto) {
		this.addressDto = addressDto;
	}
	public Long getBoundaryId() {
		return boundaryId;
	}
	public void setBoundaryId(Long boundaryId) {
		this.boundaryId = boundaryId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getParentExecutiveBodyId() {
		return parentExecutiveBodyId;
	}
	public void setParentExecutiveBodyId(Long parentExecutiveBodyId) {
		this.parentExecutiveBodyId = parentExecutiveBodyId;
	}
	public boolean isRoot() {
		return root;
	}
	public void setRoot(boolean root) {
		this.root = root;
	}
	
}
