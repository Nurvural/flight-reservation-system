package com.flightreservation.user_service;

public class Not {
	/*
	 * CustomUserDetailsService → login sırasında DB’den user çekiyor
	 * 
	 * JwtUtil → token üretme/doğrulama
	 * 
	 * JwtAuthenticationFilter → her request’te token kontrol ediyor
	 * 
	 * SecurityConfig → hangi endpoint’e kim erişebilir kuralları
	 */

	/*
	 * OncePerRequestFilter sınıfından override ettiğimiz metod.
	 * 
	 * Her HTTP isteği geldiğinde bir kere çalışır.
	 * 
	 * Amacı: gelen request üzerinde kendi güvenlik kontrollerimizi yapıp, zinciri
	 * devam ettirmek.
	 * 
	 * HttpServletRequest request → gelen HTTP isteği (header, body, path vs.
	 * içerir)
	 * 
	 * HttpServletResponse response → cevap objesi, istersen buraya direkt hata veya
	 * mesaj yazabilirsin
	 * 
	 * FilterChain filterChain → filter zinciri, diğer filter’lara ve controller’a
	 * geçişi sağlar
	 * 
	 * 
	 * Akış Özeti (Request → Response)
	 * 
	 * Login isteği (/api/auth/login)
	 * 
	 * Controller → AuthenticationManager →
	 * CustomUserDetailsService.loadUserByUsername() → DB’den user
	 * 
	 * Password kontrolü → başarılı ise JWT üretilir (JwtUtil.generateToken())
	 * 
	 * Response: token + rol + email
	 * 
	 * Sonraki her istek
	 * 
	 * JwtAuthenticationFilter devreye girer → header’dan token alır
	 * 
	 * Token doğrulama (JwtUtil.validateToken())
	 * 
	 * Eğer geçerli → SecurityContextHolder’a user yazılır
	 * 
	 * SecurityConfig kuralları uygulanır → endpoint’e erişim kontrol edilir
	 * 
	 * 
	 * 
	 * Kullanıcı “Forgot Password” isteği gönderir

Endpoint: POST /api/auth/forgot-password

Body: { "email": "kullanici@example.com" }

Service’de:

Email ile kullanıcı DB’den bulunur

UUID ile reset token üretilir ve user.resetToken olarak kaydedilir

Token, kullanıcıya (genellikle email ile) gönderilir

Kullanıcı gelen token ile “Reset Password” isteği gönderir

Endpoint: POST /api/auth/reset-password

Body: { "token": "...", "newPassword": "yeniSifre" }

Service’de:

Token DB’de kullanıcıya karşılık geliyor mu kontrol edilir

Eğer geçerli ise user.password = passwordEncoder.encode(newPassword) yapılır

user.resetToken null yapılır

Veritabanında ekstra alan yok

Kullanıcı entity’sinde sadece password ve resetToken var

DTO’daki newPassword sadece request için kullanılıyor

Postman Testi

Önce POST /api/auth/forgot-password → Token üretilir

Sonra POST /api/auth/reset-password → Token ve yeni şifre ile şifre güncellenir
	 */
}
