<template>
  <div class="wedding-container">
    <div v-if="loading" class="loading-screen">
      <div class="spinner"></div>
      <p>正在获取甜蜜配置...</p>
    </div>

    <div v-else class="content-wrapper">
      <div class="bg-layer" :style="bgStyle"></div>
      <div class="overlay"></div>

      <main class="main-card">
        <header class="header-section">
          <h1 class="couple-names">
            {{ config.basic.groomName }} <span class="heart">❤</span> {{ config.basic.brideName }}
          </h1>
          <p class="wedding-date">{{ formattedDate }}</p>
          <div class="location">
            <i class="icon-location">📍</i> {{ config.basic.weddingLocation }}
            <span v-if="config.basic.enableWeather && weatherData" class="weather-tag">
               | {{ weatherData }}
            </span>
          </div>
        </header>

        <section class="timer-section">
          <h2 class="timer-title">{{ timerTitle }}</h2>
          <div class="timer-display">
            <div class="time-box">
              <span class="num">{{ timeData.days }}</span>
              <span class="label">天</span>
            </div>
            <div class="time-box">
              <span class="num">{{ timeData.hours }}</span>
              <span class="label">时</span>
            </div>
            <div class="time-box">
              <span class="num">{{ timeData.minutes }}</span>
              <span class="label">分</span>
            </div>
            <div class="time-box">
              <span class="num">{{ timeData.seconds }}</span>
              <span class="label">秒</span>
            </div>
          </div>
        </section>

        <section class="timeline-preview" v-if="config.timeline.timelineNodes.length">
          <h3>📅 婚礼流程预览</h3>
          <ul class="timeline-list">
            <li v-for="(node, index) in config.timeline.timelineNodes" :key="index">
              <div class="node-time">
                {{ node.startTime }}
                <span v-if="node.endTime">- {{ node.endTime }}</span>
              </div>
              <div class="node-content">
                <strong>{{ node.title }}</strong>
                <span class="desc">{{ node.description }}</span>
              </div>
            </li>
          </ul>
        </section>

        <footer class="footer-controls">
          <button @click="toggleMusic" class="music-btn" :class="{ playing: isMusicPlaying }">
            {{ isMusicPlaying ? '🎵 正在播放' : '🔇 点击播放音乐' }}
          </button>

          <nav class="quick-menu">
            <a href="javascript:;" @click="scrollTo('gallery')">相册</a>
            <a v-if="config.basic.mapLocation" :href="config.basic.mapLocation" target="_blank">导航</a>
            <a href="javascript:;" @click="handleShare">分享</a>
          </nav>
        </footer>
      </main>

      <audio ref="audioRef" :loop="config.aplayer.order === 'loop'" crossorigin="anonymous"></audio>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';

// --- 1. 定义与 API 返回完全一致的类型 ---

interface BasicConfig {
  cityName: string;
  enableWeather: boolean;
  shareImage: string;
  weddingLocation: string;
  brideName: string;
  weddingDate: string; // "2026-10-01 12:00"
  groomName: string;
  mapLocation: string;
}

interface TimelineNode {
  title: string;
  description: string;
  type: string; // "timepoint" | "timespan"
  startTime: string;
  endTime?: string;
}

interface AlbumConfig {
  pageSize: number;
  displayMode: string;
}

interface APlayerConfig {
  volume: number;
  server: string; // "netease"
  host: string;   // "https://api.injahow.cn/meting/"
  theme: string;
  id: number;     // 2345868969
  type: string;   // "playlist"
  preload: string;
  autoplay: boolean;
  order: string;
}

interface RootConfig {
  basic: BasicConfig;
  timeline: {
    timelineNodes: TimelineNode[];
  };
  album: AlbumConfig;
  aplayer: APlayerConfig;
  theme: {
    customCss: string;
  };
}

// --- 2. 状态管理 ---

const loading = ref(true);
const weatherData = ref('22°C 晴'); // 暂时 Mock，后续可接真实接口
const audioRef = ref<HTMLAudioElement | null>(null);
const isMusicPlaying = ref(false);
const currentSongUrl = ref('');

// 默认空数据结构
const config = ref<RootConfig>({
  basic: {
    cityName: '', enableWeather: false, shareImage: '', weddingLocation: '',
    brideName: '', weddingDate: '', groomName: '', mapLocation: ''
  },
  timeline: { timelineNodes: [] },
  album: { pageSize: 10, displayMode: 'waterfall' },
  aplayer: { volume: 0.7, server: 'netease', host: '', theme: '', id: 0, type: 'playlist', preload: 'none', autoplay: false, order: 'random' },
  theme: { customCss: '' }
});

// --- 3. 核心逻辑 ---

// 获取配置
const fetchConfig = async () => {
  try {
    // 使用你提供的真实接口地址
    const res = await fetch('/apis/api.wedding.aiym.fun/v1alpha1/config');
    if (!res.ok) throw new Error('API Error');
    const data: RootConfig = await res.json();
    config.value = data;

    // 初始化音乐 (解析 Meting API)
    await initMusic(data.aplayer);

  } catch (e) {
    console.error('获取配置失败:', e);
  } finally {
    loading.value = false;
  }
};

// 解析 Meting 接口获取真实 MP3 地址
const initMusic = async (aplayer: APlayerConfig) => {
  if (!aplayer.host || !aplayer.id) return;

  try {
    // 构造 Meting API 请求，获取歌单
    // 格式通常是: host + ?type=playlist&id=xxx&server=netease
    const apiUrl = `${aplayer.host}?type=${aplayer.type}&id=${aplayer.id}&server=${aplayer.server}`;
    const res = await fetch(apiUrl);
    const songList = await res.json();

    if (Array.isArray(songList) && songList.length > 0) {
      // 简单起见，取第一首作为背景音乐
      const firstSong = songList[0];
      if (audioRef.value && firstSong.url) {
        audioRef.value.src = firstSong.url;
        audioRef.value.volume = aplayer.volume;
      }
    }
  } catch (e) {
    console.warn('背景音乐解析失败:', e);
  }
};

// 倒计时逻辑
const timeData = ref({ days: 0, hours: 0, minutes: 0, seconds: 0 });
const timerStatus = ref<'countdown' | 'married'>('countdown');
let timerInterval: number | undefined;

const calculateTime = () => {
  const dateStr = config.value.basic.weddingDate;
  if (!dateStr) return;

  // 兼容性处理：将 "2026-10-01 12:00" 转换为 "2026-10-01T12:00"
  const targetDate = new Date(dateStr.replace(' ', 'T'));
  const now = new Date();
  const diff = targetDate.getTime() - now.getTime();

  const absDiff = Math.abs(diff);
  timerStatus.value = diff > 0 ? 'countdown' : 'married';

  timeData.value = {
    days: Math.floor(absDiff / (1000 * 60 * 60 * 24)),
    hours: Math.floor((absDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
    minutes: Math.floor((absDiff % (1000 * 60 * 60)) / (1000 * 60)),
    seconds: Math.floor((absDiff % (1000 * 60)) / 1000)
  };
};

// 计算属性
const formattedDate = computed(() => {
  if (!config.value.basic.weddingDate) return '';
  return config.value.basic.weddingDate.split(' ')[0]; // 只显示日期部分
});

const timerTitle = computed(() => {
  return timerStatus.value === 'countdown' ? '距离婚礼还有' : '我们已幸福相伴';
});

// 背景样式：优先使用 shareImage，否则使用默认渐变
const bgStyle = computed(() => {
  const img = config.value.basic.shareImage;
  // 这里的默认图你可以换成你自己的默认图
  const defaultImg = 'https://images.unsplash.com/photo-1519741497674-611481863552?q=80&w=2070&auto=format&fit=crop';
  const url = img || defaultImg;
  return {
    backgroundImage: `url(${url})`
  };
});

// --- 交互 ---

const toggleMusic = () => {
  if (!audioRef.value) return;
  if (isMusicPlaying.value) {
    audioRef.value.pause();
  } else {
    audioRef.value.play();
  }
  isMusicPlaying.value = !isMusicPlaying.value;
};

const scrollTo = (id: string) => {
  console.log('Scroll to', id);
  // 实现页面内锚点跳转逻辑
};

const handleShare = () => {
  alert('链接已复制，快去分享给好友吧！');
};

// --- 生命周期 ---

onMounted(() => {
  fetchConfig();
  timerInterval = window.setInterval(calculateTime, 1000);
});

onUnmounted(() => {
  if (timerInterval !== undefined) {
    clearInterval(timerInterval);
    timerInterval = undefined;
  }
});
</script>

<style scoped>
/* 1. 基础布局 */
.wedding-container {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  position: relative;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  color: #fff;
}

/* 2. 背景层 */
.bg-layer {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background-size: cover;
  background-position: center;
  z-index: 0;
  transform: scale(1.05); /* 轻微放大防止白边 */
  filter: blur(2px); /* 轻微模糊背景让文字更清晰 */
}

.overlay {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.35); /* 黑色遮罩 */
  z-index: 1;
}

/* 3. 卡片容器 */
.content-wrapper {
  position: relative;
  z-index: 10;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.main-card {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  padding: 2rem;
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  width: 90%;
  max-width: 480px;
  max-height: 90vh;
  overflow-y: auto; /* 内容过多可滚动 */
  text-align: center;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* 隐藏滚动条 */
.main-card::-webkit-scrollbar { width: 0; }

/* 4. 文本样式 */
.couple-names { font-size: 1.8rem; margin: 0; font-weight: 500; text-shadow: 0 2px 4px rgba(0,0,0,0.3); }
.heart { color: #ff6b81; display: inline-block; animation: beat 1.2s infinite; }
.wedding-date { font-size: 1.1rem; opacity: 0.9; margin: 0.5rem 0; }
.location { font-size: 0.9rem; opacity: 0.8; background: rgba(0,0,0,0.2); display: inline-block; padding: 4px 12px; border-radius: 12px; }

/* 5. 倒计时 */
.timer-title { font-size: 0.9rem; text-transform: uppercase; letter-spacing: 2px; opacity: 0.8; margin-bottom: 0.5rem; }
.timer-display { display: flex; justify-content: center; gap: 1rem; }
.time-box { background: rgba(255,255,255,0.1); padding: 0.5rem; border-radius: 8px; min-width: 50px; }
.time-box .num { display: block; font-size: 1.5rem; font-weight: bold; line-height: 1.1; }
.time-box .label { font-size: 0.7rem; opacity: 0.7; }

/* 6. 流程列表 */
.timeline-preview { text-align: left; background: rgba(0,0,0,0.2); padding: 1rem; border-radius: 12px; }
.timeline-preview h3 { font-size: 0.95rem; margin: 0 0 0.8rem 0; border-bottom: 1px solid rgba(255,255,255,0.2); padding-bottom: 0.5rem; }
.timeline-list { list-style: none; padding: 0; margin: 0; }
.timeline-list li { display: flex; gap: 1rem; margin-bottom: 0.8rem; font-size: 0.9rem; }
.timeline-list li:last-child { margin-bottom: 0; }
.node-time { font-weight: bold; color: #ffeaa7; min-width: 85px; text-align: right; }
.node-content { display: flex; flex-direction: column; }
.node-content .desc { font-size: 0.75rem; opacity: 0.7; margin-top: 2px; }

/* 7. 底部控制 */
.footer-controls { margin-top: auto; padding-top: 1rem; border-top: 1px solid rgba(255,255,255,0.1); }
.music-btn { background: transparent; border: 1px solid rgba(255,255,255,0.6); color: #fff; padding: 6px 16px; border-radius: 20px; cursor: pointer; transition: 0.3s; font-size: 0.9rem; margin-bottom: 1rem; }
.music-btn.playing { background: rgba(76, 209, 55, 0.3); border-color: #4cd137; color: #4cd137; }

.quick-menu { display: flex; justify-content: center; gap: 2rem; }
.quick-menu a { color: #fff; text-decoration: none; opacity: 0.7; font-size: 0.9rem; transition: opacity 0.3s; }
.quick-menu a:hover { opacity: 1; }

/* 动画 */
@keyframes beat { 0%, 100% { transform: scale(1); } 50% { transform: scale(1.2); } }
.spinner { width: 30px; height: 30px; border: 3px solid rgba(255,255,255,0.3); border-radius: 50%; border-top-color: #fff; animation: spin 1s infinite linear; margin-bottom: 1rem; }
@keyframes spin { to { transform: rotate(360deg); } }
.loading-screen { height: 100vh; display: flex; flex-direction: column; justify-content: center; align-items: center; background: #222; }
</style>
