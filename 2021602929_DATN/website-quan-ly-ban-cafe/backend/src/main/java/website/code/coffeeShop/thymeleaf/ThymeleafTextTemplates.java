package website.code.coffeeShop.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Using text templates with Thymeleaf.
 * <p>
 * See http://blog.codeleak.pl/2017/03/getting-started-with-thymeleaf-3-text.html
 */
@Controller
@RequestMapping("/text-templates")
public class ThymeleafTextTemplates {
//    @Autowired
//    HomeService homeService;
//
//    private TemplateEngine textTemplateEngine;
//
//    public ThymeleafTextTemplates(TemplateEngine textTemplateEngine) {
//        this.textTemplateEngine = textTemplateEngine;
//    }
//
//    @GetMapping("/form")
//    public String form(Model model) {
//        List<String> list = Collections.emptyList();
//        Page<String> page = new PageImpl<>(list);
//        model.addAttribute("DoUongPage",  page);
//        model.addAttribute("pageNumbers", 0);
//        model.addAttribute(new Form());
//        return "th-form";
//    }
//    @PostMapping("/save")
//    public String postForm(@ModelAttribute Form form, Model model) {
//
////        Context context = new Context();
//        DoUongRequest doUongRequest = new DoUongRequest();
//        doUongRequest.setProductName(form.getName());
//        doUongRequest.setPrice(form.getPrice());
//        homeService.SaveDoUong(doUongRequest);
//
//        return "redirect:/text-templates/formSearch";
//    }
//
//
//
//    @GetMapping(value = "/formSearch")
//    public String listBooks(
//            Model model,
//            @RequestParam("page") Optional<Integer> page,
//            @RequestParam("size") Optional<Integer> size) {
//        int currentPage = page.orElse(1);//cho nay co nghia la neu  null thi mac dinh se la 1
//        int pageSize = size.orElse(5);//cho nay co nghia la neu  null thi mac dinh se la 5
//
//        PageableDTO pageableDTO = new PageableDTO(currentPage-1, pageSize);
//        Page<DoUong> doUongPage = homeService.findPaginated(pageableDTO);
//
//        model.addAttribute("DoUongPage", doUongPage);
//
//        int totalPages = doUongPage.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                    .boxed()
//                    .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
//
//        return "th-form";
//    }
//
//    @Configuration
//    public static class ThymeleafConfig {
//
//        @Bean(name = "textTemplateEngine")
//        public TemplateEngine textTemplateEngine() {
//            TemplateEngine templateEngine = new TemplateEngine();
//            templateEngine.addTemplateResolver(textTemplateResolver());
//            return templateEngine;
//        }
//
//        private ITemplateResolver textTemplateResolver() {
//            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//            templateResolver.setPrefix("/templates/text/");
//            templateResolver.setSuffix(".txt");
//            templateResolver.setTemplateMode(TemplateMode.TEXT /* https://github.com/thymeleaf/thymeleaf/issues/395 */);
//            templateResolver.setCharacterEncoding("UTF8");
//            templateResolver.setCheckExistence(true);
//            templateResolver.setCacheable(false);
//            return templateResolver;
//        }
//    }
//
//    @Data
//    @NoArgsConstructor
//    public static class Form {
//        private String name = "spring.io";
//        private String url = "http://spring.io";
//        private String tags = "#spring #framework #java";
//        private Float price = 0f;
//    }

}
