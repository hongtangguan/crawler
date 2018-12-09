package com.juban.utils;

public class Demo
{

/*    public static void main(String[] args) {
*//*        String s = "<p>已上市<em class=\"vline\"></em>1000-9999人<em class=\"vline\"></em><a ka=\"job-detail-brandindustry\" href=\"/i100020/\">互联网</a></p>";
        Html html = new Html(s);
        String s1 = html.xpath("//p/html()").toString().replaceAll("<em..*", "");

        System.out.println(html.xpath("//p/html()").toString().replaceAll("<em..*",""));

        System.out.println(html.xpath("//p/text()").toString().replaceAll(s1,""));
//
        System.out.println(html.xpath("//p/a/text()"));*//*

      *//*  Html html = new Html("<p>20-99人<em class=\"vline\"></em><a ka=\"job-detail-brandindustry\" href=\"/i100020/\">互联网</a></p>");
        String s1 = html.xpath("//p/html()").toString().replaceAll("<em..*", "");

        System.out.println(html.xpath("//p/html()").toString().replaceAll("<em..*",""));

        System.out.println(html.xpath("//p/text()").toString().replaceAll(s1,""));*//*



    }*/



    public static void main(String[] args) {

        String s = " 111111岗位职责1、参与产品初期的需求定义；2、根据需求快速完成开发方案设计；3、高效完成功能开发；\n" +
                " 4、功能自测、代码定期自查、框架及系统完善；5、参与日常的功能快速迭代。\n" +
                " \n" +
                " 任职要求1、五年PHP开发经验，精通熟练PHP框架，熟悉Laravel、Symfony、Yii等主流框架其中至少一种；2、熟练使用MySQL或其他大型数据库，对索引、锁和事务有较深入的了解，有并发处理经验，有相关的调优经验；3、熟练使用Redis、Memcache等常见非关系型数据机制；4、熟练HTML、CSS、Javascript、Jquery等前端技术；5、熟练Linux/UNIX等操作系统，有生产服务器运维经验的优先；6、熟悉或掌握一种或以上其他服务端语言的优先；\n" +
                " 7、有大中型网站建设，高并发处理能力等经验的优先；8、有责任心，具备良好的编码习惯，较强的学习及沟通能力。 ";


        if (s.contains("任职要求")) {

            String sss = s.substring(0, s.indexOf("任职要求"));
            System.out.println(sss);

            String ss = s.substring(s.indexOf("任职要求"));
            System.out.println(ss);


        }



    }








}
