package kr.hhplus.be.server.interfaces.bestItems;

import kr.hhplus.be.server.application.bestItem.BestItemsCacheDto;
import kr.hhplus.be.server.domain.bestItem.BestItemService;
import kr.hhplus.be.server.interfaces.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/api/v1/bestItems")
@RequiredArgsConstructor
@RestController
public class BestItemController {

	private final BestItemService bestItemService;

	@GetMapping
	public ApiResponse<BestItemResponse.BestItems> getBestItemsTop10() {
		List<BestItemsCacheDto> resultDto = bestItemService.getTop10BestItems();

		return ApiResponse.success(BestItemResponse.BestItems.of(resultDto));
	}
}
