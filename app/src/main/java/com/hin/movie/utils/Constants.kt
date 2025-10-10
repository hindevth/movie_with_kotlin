package com.hin.movie.utils

import com.hin.movie.data.entities.Country
import com.hin.movie.data.entities.Genre
import com.hin.movie.data.entities.Sort

object Constants {
    const val APP_DOMAIN_CDN_IMAGE = "https://phimimg.com"
    const val APP_DOMAIN_FRONTEND = "https://phimapi.com"

    val sort = listOf(
        Sort("Mới nhất", "desc"),
        Sort("Cũ nhất", "asc"),
    )
    val region = listOf(
        Country("f6ce1ae8b39af9d38d653b8a0890adb8", "Việt Nam", "viet-nam"),
        Country("3e075636c731fe0f889c69e0bf82c083", "Trung Quốc", "trung-quoc"),
        Country("cefbf1640a17bad1e13c2f6f2a811a2d", "Thái Lan", "thai-lan"),
        Country("dcd5551cbd22ea2372726daafcd679c1", "Hồng Kông", "hong-kong"),
        Country("92f688188aa938a03a61a786d6616dcb", "Pháp", "phap"),
        Country("24a5bf049aeef94ab79bad1f73f16b92", "Đức", "duc"),
        Country("41487913363f08e29ea07f6fdfb49a41", "Hà Lan", "ha-lan"),
        Country("8dbb07a18d46f63d8b3c8994d5ccc351", "Mexico", "mexico"),
        Country("61709e9e6ca6ca8245bc851c0b781673", "Thụy Điển", "thuy-dien"),
        Country("77dab2f81a6c8c9136efba7ab2c4c0f2", "Philippines", "philippines"),
        Country("208c51751eff7e1480052cdb4e26176a", "Đan Mạch", "dan-mach"),
        Country("69e561770d6094af667b9361f58f39bd", "Thụy Sĩ", "thuy-si"),
        Country("c338f80e38dd2381f8faf9eccb6e6c1c", "Ukraina", "ukraina"),
        Country("05de95be5fc404da9680bbb3dd8262e6", "Hàn Quốc", "han-quoc"),
        Country("74d9fa92f4dea9ecea8fc2233dc7921a", "Âu Mỹ", "au-my"),
        Country("aadd510492662beef1a980624b26c685", "Ấn Độ", "an-do"),
        Country("445d337b5cd5de476f99333df6b0c2a7", "Canada", "canada"),
        Country("8a40abac202ab3659bb98f71f05458d1", "Tây Ban Nha", "tay-ban-nha"),
        Country("4647d00cf81f8fb0ab80f753320d0fc9", "Indonesia", "indonesia"),
        Country("59317f665349487a74856ac3e37b35b5", "Ba Lan", "ba-lan"),
        Country("3f0e49c46cbde0c7adf5ea04a97ab261", "Malaysia", "malaysia"),
        Country("fcd5da8ea7e4bf894692933ee3677967", "Bồ Đào Nha", "bo-dao-nha"),
        Country("b6ae56d2d40c99fc293aefe45dcb3b3d", "UAE", "uae"),
        Country("471cdb11e01cf8fcdafd3ab5cd7b4241", "Châu Phi", "chau-phi"),
        Country("cc85d02a69f06f7b43ab67f5673604a3", "Ả Rập Xê Út", "a-rap-xe-ut"),
        Country("d4097fbffa8f7149a61281437171eb83", "Nhật Bản", "nhat-ban"),
        Country("559fea9881e3a6a3e374b860fa8fb782", "Đài Loan", "dai-loan"),
        Country("932bbaca386ee0436ad0159117eabae4", "Anh", "anh"),
        Country("45a260effdd4ba38e861092ae2a1b96a", "Quốc Gia Khác", "quoc-gia-khac"),
        Country("8931caa7f43ee5b07bf046c8300f4eba", "Thổ Nhĩ Kỳ", "tho-nhi-ky"),
        Country("2dbf49dd0884691f87e44769a3a3a29e", "Nga", "nga"),
        Country("435a85571578e419ed511257881a1e75", "Úc", "uc"),
        Country("42537f0fb56e31e20ab9c2305752087d", "Brazil", "brazil"),
        Country("a30878a7fdb6a94348fce16d362edb11", "Ý", "y"),
        Country("638f494a6d33cf5760f6e95c8beb612a", "Na Uy", "na-uy"),
        Country("3cf479dac2caaead12dfa36105b1c402", "Nam Phi", "nam-phi")
    )

    val genre = listOf(
        Genre("9822be111d2ccc29c7172c78b8af8ff5", "Hành Động", "hanh-dong"),
        Genre("d111447ee87ec1a46a31182ce4623662", "Miền Tây", "mien-tay"),
        Genre("0c853f6238e0997ee318b646bb1978bc", "Trẻ Em", "tre-em"),
        Genre("f8ec3e9b77c509fdf64f0c387119b916", "Lịch Sử", "lich-su"),
        Genre("3a17c7283b71fa84e5a8d76fb790ed3e", "Cổ Trang", "co-trang"),
        Genre("1bae5183d681b7649f9bf349177f7123", "Chiến Tranh", "chien-tranh"),
        Genre("68564911f00849030f9c9c144ea1b931", "Viễn Tưởng", "vien-tuong"),
        Genre("4db8d7d4b9873981e3eeb76d02997d58", "Kinh Dị", "kinh-di"),
        Genre("1645fa23fa33651cef84428b0dcc2130", "Tài Liệu", "tai-lieu"),
        Genre("2fb53017b3be83cd754a08adab3e916c", "Bí Ẩn", "bi-an"),
        Genre("4b4457a1af8554c282dc8ac41fd7b4a1", "Phim 18+", "phim-18"),
        Genre("bb2b4b030608ca5984c8dd0770f5b40b", "Tình Cảm", "tinh-cam"),
        Genre("a7b065b92ad356387ef2e075dee66529", "Tâm Lý", "tam-ly"),
        Genre("591bbb2abfe03f5aa13c08f16dfb69a2", "Thể Thao", "the-thao"),
        Genre("66c78b23908113d478d8d85390a244b4", "Phiêu Lưu", "phieu-luu"),
        Genre("252e74b4c832ddb4233d7499f5ed122e", "Âm Nhạc", "am-nhac"),
        Genre("a2492d6cbc4d58f115406ca14e5ec7b6", "Gia Đình", "gia-dinh"),
        Genre("01c8abbb7796a1cf1989616ca5c175e6", "Học Đường", "hoc-duong"),
        Genre("ba6fd52e5a3aca80eaaf1a3b50a182db", "Hài Hước", "hai-huoc"),
        Genre("7a035ac0b37f5854f0f6979260899c90", "Hình Sự", "hinh-su"),
        Genre("578f80eb493b08d175c7a0c29687cbdf", "Võ Thuật", "vo-thuat"),
        Genre("0bcf4077916678de9b48c89221fcf8ae", "Khoa Học", "khoa-hoc"),
        Genre("2276b29204c46f75064735477890afd6", "Thần Thoại", "than-thoai"),
        Genre("37a7b38b6184a5ebd3c43015aa20709d", "Chính Kịch", "chinh-kich"),
        Genre("268385d0de78827ff7bb25c35036ee2a", "Kinh Điển", "kinh-dien")
    )

    val year = listOf(
        "2012",
        "2013",
        "2014",
        "2015",
        "2016",
        "2017",
        "2018",
        "2019",
        "2020",
        "2021",
        "2022",
        "2023",
        "2024",
        "2025"
    )
}

enum class EActivityOptionAnim {
    STACK, SLIDE, FADE
}

object MovieType {
    const val PHIM_BO = "phim-bo"
    const val PHIM_LE = "phim-le"
    const val HOAT_HINH = "hoat-hinh"
    const val TV_SHOWS = "tv-shows"
    const val VIETSUB = "phim-vietsub"
    const val THUYET_MINH = "phim-thuyet-minh"
    const val LONG_TIENG = "phim-long-tieng"
}