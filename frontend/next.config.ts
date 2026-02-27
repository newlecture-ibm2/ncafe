import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    // 이미지는 실제 백엔드(Spring Boot) 서버에서 가져와야 하므로 API_BASE_URL(8081)을 사용합니다.
    const backendUrl = process.env.API_BASE_URL || 'http://localhost:8081';
    return [
      {
        source: '/images/:path*',
        destination: `${backendUrl}/:path*`,
      },
    ];
  },
  images: {
    unoptimized: true, // 로컬 개발용
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'images.unsplash.com',
      },
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8081',
      },
    ],
  },
};

export default nextConfig;

// DDD 방법론? 철학? 