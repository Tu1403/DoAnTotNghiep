CREATE TABLE Roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(60) UNIQUE NOT NULL
);

INSERT INTO Roles (role_id, role_name)
VALUES
(1, 'customer'),
(2, 'warehouse staff'),
(3, 'waiter'),
(4, 'cashier'),
(5, 'shop owner');

CREATE TABLE Category (
    cid SERIAL PRIMARY KEY,
    group_name VARCHAR(60) NOT NULL,
    category_name VARCHAR(60) NOT NULL,
    description VARCHAR(300) NOT NULL
);

INSERT INTO Category (group_name, category_name, description)
VALUES
('Cà Phê', 'Cà phê highligt', 'Những loại cà phê hảo hạng, nổi bật với hương vị đậm đà và phong phú của cà phê Việt Nam.'),
('Cà Phê', 'Cà phê Việt Nam', 'Cà phê truyền thống Việt Nam, nổi tiếng với hương vị mạnh mẽ và đậm đà.'),
('Cà Phê', 'Cà phê máy', 'Các loại đồ uống từ espresso với một chút biến tấu hiện đại, hoàn hảo cho những người yêu thích cà phê.'),
('Cà Phê', 'Cold brew', 'Cà phê pha lạnh mịn màng và sảng khoái với hương vị dịu nhẹ, đặc biệt.'),
('Trà trái cây - Hitea', 'Trà trái cây', 'Các loại trà trái cây tươi mát, mang đến sự cân bằng tuyệt vời giữa vị ngọt và chua.'),
('Trà trái cây - Hitea', 'Hi-tea', 'Các loại trà sáng tạo, kết hợp những nguyên liệu tốt nhất cho trải nghiệm tươi mới.'),
('Trà xanh - Chocolate', 'Trà xanh tây bắc', 'Trà xanh Tây Bắc chính gốc, nổi bật với hương vị đất và chút đắng nhẹ.'),
('Trà xanh - Chocolate', 'Chocolate', 'Các loại đồ uống chocolate đậm đà và mịn màng, hoàn hảo để thưởng thức như một món ngọt.');

CREATE TABLE Users (
    uid SERIAL PRIMARY KEY,
    fullname VARCHAR(60),
    dob DATE,
    email VARCHAR(60) UNIQUE NOT NULL,
    phone VARCHAR(60) UNIQUE NOT NULL,
    address VARCHAR(60) NOT NULL,
    avatar TEXT,
    username VARCHAR(60) UNIQUE NOT NULL,
    pass VARCHAR(60) NOT NULL,
    role_id INT NOT NULL,
    status INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);

INSERT INTO Users (uid, fullname, dob, email, phone, address, avatar, username, pass, role_id, status)
VALUES
    (3, 'User Name 3', '1990-01-01', 'user3@example.com', '123456789', 'Address 3', NULL, 'username3', 'password3', 1, 1),
    (4, 'User Name 4', '1990-01-02', 'user4@example.com', '123456780', 'Address 4', NULL, 'username4', 'password4', 1, 1),
    (5, 'User Name 5', '1990-01-03', 'user5@example.com', '123456781', 'Address 5', NULL, 'username5', 'password5', 1, 1);

CREATE TABLE Product (
    pid SERIAL PRIMARY KEY,
    pname VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(500),
    unit VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    price NUMERIC NOT NULL,
    image TEXT,
    category_id INT,
    discount FLOAT,
    FOREIGN KEY (category_id) REFERENCES Category(cid)
);

INSERT INTO Product(pname, description, unit, quantity, price, image, category_id, discount)
VALUES
('Trà Xanh Espresso Marble', 'Cho ngày thêm tươi, tỉnh, êm, mượt với Trà Xanh Espresso Marble. Đây là sự mai mối bất ngờ giữa trà xanh Tây Bắc vị mộc và cà phê Arabica Đà Lạt.', 'gram', 50, 49000.0, 'https://product.hstatic.net/1000075078/product/1696220139_tra-xanh-espresso-marble_26a0eb92bfd649508d0e93173e9b3083.jpg', 1, 0.0),
('Bơ Arabica', 'Bơ sáp Đắk Lắk dẻo quẹo hòa quyện cùng Cà phê Arabica Cầu Đất êm mượt', 'gram', 50, 49000.0, 'https://product.hstatic.net/1000075078/product/1719825409_bo-arabica_0807b34b76a34d318a4d85e3bc68dbbb.jpg', 2, 0.0),
('Đường Đen Sữa Đá', 'Nếu chuộng vị cà phê đậm đà, bùng nổ và thích vị đường đen ngọt thơm, Đường Đen Sữa Đá đích thị là thức uống dành cho bạn', 'gram', 50, 45000.0, 'https://product.hstatic.net/1000075078/product/1686716532_dd-suada_c180c6187e644babbac7019a2070231e.jpg', 2, 0.0),
('The Coffee House Sữa Đá', 'The Coffee House Sữa Đá mang hương vị hài hoà đầy lôi cuốn', 'gram', 50, 39000.0, 'https://product.hstatic.net/1000075078/product/1675355354_bg-tch-sua-da-no_4fbf208885ed464ab4b5e145336d42a2.jpg', 2, 0.0),
('Cà Phê Sữa Đá', 'Cà phê Đắk Lắk nguyên chất được pha phin truyền thống kết hợp với sữa đặc tạo nên hương vị đậm đà, hài hòa', 'gram', 50, 29000.0, 'https://product.hstatic.net/1000075078/product/1669736835_ca-phe-sua-da_e6168b6a38ec45d2b4854d2708b5d542.png', 2, 0.0),
('Cà Phê Sữa Nóng', 'Cà phê được pha phin truyền thống kết hợp với sữa đặc tạo nên hương vị đậm đà, hài hòa', 'gram', 50, 39000.0, 'https://product.hstatic.net/1000075078/product/1639377770_cfsua-nong_9a47f58888e7444a9979e0d117d49ad3.jpg', 2, 0.0),
('Bạc Sỉu', 'Thức uống này rất phù hợp những ai vừa muốn trải nghiệm chút vị đắng của cà phê vừa muốn thưởng thức vị ngọt béo ngậy từ sữa.', 'gram', 50, 29000.0, 'https://product.hstatic.net/1000075078/product/1639377904_bac-siu_525b9fa5055b41f183088c8e479a9472.jpg', 2, 0.0),
('Đường Đen Marble Latte', 'Sự kết hợp đầy mới mẻ của cà phê và đường đen cũng tạo nên diện mạo phân tầng đẹp mắt.', 'gram', 50, 55000.0, 'https://product.hstatic.net/1000075078/product/1686716537_dd-latte_785591205184481597a2a79b9331e03b.jpg', 3, 0.0),
('Caramel Macchiato Đá', 'Khuấy đều trước khi sử dụng Caramel Macchiato sẽ mang đến một sự ngạc nhiên thú vị khi vị thơm béo của bọt sữa', 'gram', 50, 55000.0, 'https://product.hstatic.net/1000075078/product/caramel-macchiato_143623_0e070a39d0e54e808db4d91049430b51.jpg', 3, 0.0),
('Cold Brew Kim Quất', 'Vị chua ngọt của Kim Quất làm dậy lên hương vị trái cây tự nhiên vốn sẵn có trong hạt cà phê Arabica Cầu Đất', 'gram', 50, 49000.0, 'https://product.hstatic.net/1000075078/product/1716811739_cold-brew-kim-quat-2_accd458085414678b3f86996899fe92f.jpg', 4, 0.0),
('Cold Brew Phúc Bồn Tử', 'Vị chua ngọt của trái phúc bồn tử, làm dậy lên hương vị trái cây tự nhiên vốn sẵn có trong hạt cà phê', 'gram', 50, 49000.0, 'https://product.hstatic.net/1000075078/product/1675329120_coldbrew-pbt_127e09b0000c4027992bc3168899a656.jpg', 4, 0.0),
('Oolong Tứ Quý Sen', 'Trà hạt sen là thức uống thanh mát, nhẹ nhàng phù hợp cho cả buổi sáng và chiều tối.', 'cup', 50, 49000.0, 'https://product.hstatic.net/1000075078/product/tra-sen_905594_ead03605b9e94af995c2df587a7dac17.jpg', 5, 0.0),
('Trà Đào Cam Sả Đá', 'Vị thanh ngọt của đào, vị chua dịu của Cam Vàng nguyên vỏ, vị chát của trà đen tươi được ủ mới mỗi 4 tiếng', 'cup', 50, 49000.0, 'https://product.hstatic.net/1000075078/product/1669736819_tra-dao-cam-sa-da_63defc32ce214da487850604a63ff281.png', 5, 0.0),
('Hi Tea Đào Kombucha', 'Trà hoa Hibiscus 0% caffeine chua nhẹ, kết hợp cùng trà lên men Kombucha hoàn toàn tự nhiên', 'cup', 50, 59000.0, 'https://product.hstatic.net/1000075078/product/1686716517_kombucha-dao_934743c8b4ed46e994963098b81f02b4.jpg', 6, 0.0),
('Hi-Tea Yuzu Kombucha', 'Trà hoa Hibiscus 0% caffeine chua nhẹ, kết hợp cùng trà lên men Kombucha hoàn toàn tự nhiên', 'cup', 50, 59000.0, 'https://product.hstatic.net/1000075078/product/1686716517_kombucha-dao_934743c8b4ed46e994963098b81f02b4.jpg', 6, 0.0),
('Trà Xanh Latte', 'Trà xanh Tây Bắc vị thanh, chát nhẹ hoà cùng sữa tươi nguyên kem ngọt béo tạo nên hương vị dễ uống, dễ yêu.', 'cup', 50, 45000.0, 'https://product.hstatic.net/1000075078/product/1697450388_tx-latte_ef8fdb94fb2a4691b0cc909188b77829.jpg', 7, 0.0),
('Trà Xanh Đường Đen', 'Trà Xanh Đường Đen với hiệu ứng phân tầng đẹp mắt, như phác hoạ núi đồi Tây Bắc bảng lảng trong sương mây, thấp thoáng những nương chè xanh ngát.', 'cup', 50, 55000.0, 'https://product.hstatic.net/1000075078/product/1697450399_tx-duong-den_3342d63e65df4bd7a264ca681b9e30f1.jpg', 7, 0.0),
('Chocolate Nóng', 'Vị ngọt tự nhiên, không gắt cổ, để lại một chút đắng nhẹ, cay cay trên đầu lưỡi.', 'cup', 50, 55000.0, 'https://product.hstatic.net/1000075078/product/chocolatenong_949029_d045f95f84314bbe9273f58b6580f2f3.jpg', 8, 0.0),
('Chocolate Đá', 'Vị ngọt tự nhiên, không gắt cổ, để lại một chút đắng nhẹ, cay cay trên đầu lưỡi.', 'cup', 50, 55000.0, 'https://product.hstatic.net/1000075078/product/chocolate-da_877186_e9f44159fc884e03bab3defac5d7dd73.jpg', 8, 0.0);

CREATE TABLE Cartitem (
    item_id SERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    total_cost NUMERIC NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(pid),
    FOREIGN KEY (user_id) REFERENCES Users(uid)
);

CREATE TABLE Dinnertable (
    table_id INT PRIMARY KEY NOT NULL,
    number_of_chair INT NOT NULL,
    status INT NOT NULL
);

INSERT INTO Dinnertable (table_id, number_of_chair, status)
VALUES
    (1, 2, 0),
    (2, 2, 0),
    (3, 2, 0),
    (4, 2, 0),
    (5, 4, 0),
    (6, 4, 0),
    (7, 4, 0),
    (8, 8, 0),
    (9, 10, 0);

CREATE TABLE Booktable (
    id SERIAL PRIMARY KEY,
    table_id INT NOT NULL,
    phone VARCHAR(60) NOT NULL,
    note TEXT,
    times TIMESTAMP NOT NULL,
    customer_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Users(uid),
    FOREIGN KEY (table_id) REFERENCES Dinnertable(table_id)
);

CREATE TABLE Bill (
    bill_id SERIAL PRIMARY KEY NOT NULL,
    phone VARCHAR(60) NOT NULL,
    address VARCHAR(100) NOT NULL,
    created_time TIMESTAMP NOT NULL,
    number_of_guest INT NOT NULL,
    total_cost NUMERIC NOT NULL,
    table_id INT,
    user_id INT,
    status INT,
    type INT,
    FOREIGN KEY (user_id) REFERENCES Users(uid)
);

CREATE TABLE Billdetail (
    quantity INT NOT NULL,
    price NUMERIC NOT NULL,
    pid INT,
    bill_id INT,
    FOREIGN KEY (pid) REFERENCES Product(pid),
    FOREIGN KEY (bill_id) REFERENCES Bill(bill_id)
);

CREATE TABLE Jobboard (
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INT,
    shift INT,
    presents INT,
    absents INT,
    FOREIGN KEY (user_id) REFERENCES Users(uid)
);

CREATE TABLE Feedback (
    fid SERIAL PRIMARY KEY NOT NULL,
    user_id INT,
    content TEXT,
    product_id INT,
    post_date VARCHAR(20),
    rate_star NUMERIC,
    status VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES Users(uid),
    FOREIGN KEY (product_id) REFERENCES Product(pid)
);

INSERT INTO Jobboard (user_id, shift, presents, absents)
VALUES
    (3, 1, 0, 0),
    (4, 2, 0, 0),
    (5, 2, 0, 0);
