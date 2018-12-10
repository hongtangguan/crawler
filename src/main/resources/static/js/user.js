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
        total: 0,
        current: 1,
        queryData: {
            rows: 10,
            page: 1,
            companyName: '',
            jobName: '',
            sources: ''
        },
        jobList:[]
    },
    methods:{
        findAll:function(type){
            if(type === 'search') {
                this.current = 1;
                this.queryData.page = 1;
            }
            //在当前方法中定义一个变量，表明是vue对象
            var _this = this;
            var rows = _this.queryData.rows;
            var page = _this.queryData.page;
            var companyName = _this.queryData.companyName;
            var jobName = _this.queryData.jobName;
            var sources = _this.queryData.sources;
            axios.get('job/getAllJobs?rows=' + rows + '&page=' + page + '&companyName=' + companyName.trim() + '&jobName=' + jobName.trim() + '&sources=' + sources.trim())
                .then(function (response) {
                    _this.jobList = response.data.result.rows;//响应数据给userList赋值
                    _this.total = response.data.result.total;
                    console.log(response);
                })
                .catch(function (error) {
                    console.log(error);
                })
        },
        pageChange: function (page) {
            this.queryData.page = page;
            this.current = page;
            this.findAll()
        },
        exportExcel:function () {
            window.location.href = 'job/excel'
        }
    },
    created:function() {//当我们页面加载的时候触发请求，查询所有
        this.findAll();
    }
});