import { defineConfig } from 'vitepress'

export default defineConfig({
  title: 'MaskSu',
  base: '/MaskSu/',
  sitemap: {
    hostname: 'https://masksu.github.io'
  },
  locales: {
    root: {
      label: 'English',
      lang: 'en-US',
      description: 'A powerful root solution with enhanced features for Android.',
      themeConfig: {
        nav: [
          { text: 'Guide', link: '/guide/what-is-masksu' }
        ],
        lastUpdatedText: 'Last updated',
        sidebar: {
          '/guide/': [
            {
              text: 'Guide',
              items: [
                { text: 'What is MaskSu?', link: '/guide/what-is-masksu' },
                { text: 'Installation', link: '/guide/installation' },
                { text: 'FAQ', link: '/guide/faq' },
                { text: 'OTA Upgrade', link: '/guide/ota' },
                { text: 'Changelog', link: '/guide/changes' }
              ]
            },
            {
              text: 'Developer',
              items: [
                { text: 'Building', link: '/guide/build' },
                { text: 'Developer Guides', link: '/guide/guides' },
                { text: 'Magisk Tools', link: '/guide/tools' },
                { text: 'Internal Details', link: '/guide/details' },
                { text: 'Android Booting', link: '/guide/boot' },
                { text: 'App Changelog', link: '/guide/app_changes' }
              ]
            }
          ]
        },
        socialLinks: [
          { icon: 'github', link: 'https://github.com/Seyud/MaskSu' }
        ],
        footer: {
          message: 'Released under the GPL3 License.',
          copyright: 'Copyright © 2024-present MaskSu developers.'
        },
        editLink: {
          pattern: 'https://github.com/Seyud/MaskSu/edit/main/website/docs/:path',
          text: 'Edit this page on GitHub'
        }
      }
    },
    zh_CN: {
      label: '简体中文',
      lang: 'zh-CN',
      description: '一个功能增强的 Android Root 解决方案。',
      themeConfig: {
        nav: [
          { text: '指南', link: '/zh_CN/guide/what-is-masksu' }
        ],
        lastUpdatedText: '最后更新',
        sidebar: {
          '/zh_CN/guide/': [
            {
              text: '指南',
              items: [
                { text: '什么是 MaskSu？', link: '/zh_CN/guide/what-is-masksu' },
                { text: '安装', link: '/zh_CN/guide/installation' },
                { text: '常见问题', link: '/zh_CN/guide/faq' },
                { text: 'OTA 升级', link: '/zh_CN/guide/ota' },
                { text: '更新日志', link: '/zh_CN/guide/changes' }
              ]
            },
            {
              text: '开发者',
              items: [
                { text: '构建', link: '/zh_CN/guide/build' },
                { text: '开发者指南', link: '/zh_CN/guide/guides' },
                { text: 'Magisk 工具', link: '/zh_CN/guide/tools' },
                { text: '内部细节', link: '/zh_CN/guide/details' },
                { text: 'Android 启动', link: '/zh_CN/guide/boot' },
                { text: '应用更新日志', link: '/zh_CN/guide/app_changes' }
              ]
            }
          ]
        },
        socialLinks: [
          { icon: 'github', link: 'https://github.com/Seyud/MaskSu' }
        ],
        footer: {
          message: '在 GPL3 许可证下发布。',
          copyright: 'Copyright © 2024-现在 MaskSu 开发者。'
        },
        editLink: {
          pattern: 'https://github.com/Seyud/MaskSu/edit/main/website/docs/:path',
          text: '在 GitHub 中编辑本页'
        }
      }
    }
  },
  head: [
    ['link', { rel: 'icon', type: 'image/png', href: '/MaskSu/logo.png' }],
    [
      'script',
      {},
      `;(() => {
        const base = '/MaskSu/'
        const path = window.location.pathname
        if (path.startsWith(base + 'zh_CN/') || path === base + 'zh_CN') return
        if (path.startsWith(base + 'guide/') || path === base) {
          const lang = (navigator.language || '').toLowerCase()
          if (lang.startsWith('zh')) {
            const sub = path.slice(base.length)
            window.location.replace(base + 'zh_CN/' + sub)
          }
        }
      })()`
    ]
  ]
})
