package org.wenzhuo.deepseekRAG.tigger.client;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wenzhuo.deepseekRAG.domain.playground.agent.tools.BookingTools;
import org.wenzhuo.deepseekRAG.domain.playground.services.FlightBookingService;

import java.util.List;

@Controller
@RequestMapping("/")
public class BookingController {

	private final FlightBookingService flightBookingService;

	public BookingController(FlightBookingService flightBookingService) {
		this.flightBookingService = flightBookingService;
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/api/bookings")
	@ResponseBody
	public List<BookingTools.BookingDetails> getBookings() {
		return flightBookingService.getBookings();
	}

}
