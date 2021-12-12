package testcase;

import java.time.LocalDate;

import com.geartracker.geartracker_backend.*;

public class RequestRepository_test{
	public static void main(String[] args) {
		RequestRepository req_repo = RequestRepository.getInstance();
		
		//Tests for createRequest
		Request new_req = new Request();
		new_req.setRequestId(1);
		new_req.setIssueDate(LocalDate.now().minusDays(10));
		new_req.setStatus("approved");
		
		//Test case for createRequest, getRequestId, and getRequestById
		req_repo.createRequest(new_req);
		
		if(req_repo.getRequestById(1).getRequestId() == 1) {
			System.out.println("Test case for createRequest passed.");
		}
		else {
			System.out.println("Test case for createRequest failed, output is + " + req_repo.getRequestById(1).getRequestId());
		}
		
		//Test case for editRequest
		new_req.setUserId("stud6");
		req_repo.editRequest(1, new_req);
		
		if(req_repo.getRequestById(1).getUserId().equals("stud6")) {
			System.out.println("Test case for editRequest passed.");
		}
		else {
			System.out.println("Test case for editRequest failed, output is + " + req_repo.getRequestById(1).getUserId());
		}
		
		//Test case for editRequestStatus
		
		new_req.setStatus("closed");
		
		if(req_repo.getRequestById(1).getStatus().equals("closed")) {
			System.out.println("Test case for editRequestStatus passed.");
		}
		else {
			System.out.println("Test case for editRequestStatus failed, output is + " + req_repo.getRequestById(1).getStatus());
		}
	}
}