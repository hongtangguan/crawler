new Vue({
    el:"#app",
    data:{
        job:{
            companyName:"",
            companyIndustry:"",
            companyPeople:"",
            companyFinancing:"",
            jobName:"",
            jobInfo:"",
            jobAddress:"",
            salary:"",
            sources:"",
        },
        jobList:[]
    },
    methods:{
        findAll:function(){
            //在当前方法中定义一个变量，表明是vue对象
            var _this = this;
            axios.get('job/getAllJobs')
                .then(function (response) {
                    _this.jobList = response.data.result.rows;//响应数据给userList赋值
                    console.log(response);
                })
                .catch(function (error) {
                    console.log(error);
                })
        }
    },
    created:function() {//当我们页面加载的时候触发请求，查询所有
        this.findAll();
    }
});