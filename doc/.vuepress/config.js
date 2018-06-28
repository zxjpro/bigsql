module.exports = {
  themeConfig: {
		sidebar: {
			'/guide/' : [
				'',
				'tips',
				'config_datasource',
				'create_logic_database',
				'create_logic_table',
                'hash_sharding',
                'master_slave',
                'full_scan_table',
                'increment_key',
                'plugin',
				'keyword',
                'not_complete',
			],
			'/dev/' : [
				'',
				'developer',
				'exception',
				'tips'
			],
			'/' : [
				''
			]
		},
    nav: [
      { text: '主页', link: '/' },
      { text: '使用文档', link: '/guide/' },
      { text: '开发文档', link: '/dev/' },
			{text: 'Languages' ,
				items : [
					{text : '中文' , link : '/Languages/chinese'},
					{text : 'englih' , link : '/Languages/english'}
				]
			},
    ],

		lastUpdated: 'Last Updated',//显示git最后提交的时间 ，以显示在页面上
		repo: 'http://www.baidu.com', //它会在每个页面的右上角显示github的链接，它也可以是一个完整的url地址
  },
	title : 'Bigsql中文文档',
	description : '一个和语言无关的分布式数据库中间件'
}
