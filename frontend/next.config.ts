import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081';
    return [
      {
        source: '/api/:path*',
        destination: `${apiUrl}/api/:path*`,
      },
      {
        source: '/images/:path*',
        destination: `${apiUrl}/:path*`,
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