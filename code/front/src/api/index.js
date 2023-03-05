import axios from "axios"
import qs from 'qs'

// let api = axios.create({baseURL: "http://localhost:8888/bookstore"})
axios.defaults.baseURL = 'http://localhost:8899/bookstore'
// axios.defaults.baseURL = 'http://47.115.231.72:8899/bookstore'
export default {
    searchBookByKey(url, pageNum, pageSize, option, key) {
        return axios({
            method: 'get',
            url: `${url}`,
            params: {
                pageNum: pageNum,
                pageSize: pageSize,
                option: option,
                key: key
            }
        })
    },
    getBookByCate(url, pageNum, pageSize, cateId) {
        return axios({
            method: 'get',
            url: `${url}`,
            params: {
                pageNum: pageNum,
                pageSize: pageSize,
                category: cateId
            }
        })
    },
    getCartBookByIds(url, bookIds) {
        return axios({
            method: 'get',
            url: `${url+ "?ids=" + bookIds}`,
        })
    },
    updateBook(url, book){
        return axios({
            method:'post',
            url:`${url}`,
            data:book,
            transformRequest:[function(data){
                return JSON.stringify(data,{allowDots:true})
            }],
            headers: {
                'Content-Type': 'application/json'
            }
        })
    },
    getBookById(url, id) {
        return axios({
            method: 'get',
            url: `${url+ "?id=" + id}`,
        })
    },
    getAllCate(url) {
        return axios({
            method: 'get',
            url: `${url}`
        })
    },
    register(url, user){
        return axios({
            method:'post',
            url:`${url}`,
            data:user,
            transformRequest:[function(data){
                return JSON.stringify(data,{allowDots:true})
            }],
            headers: {
                'Content-Type': 'application/json'
            }
        })
    },
    login(url,user){
        return axios({
            method:'post',
            url:`${url}`,
            data:user,
            transformRequest:[function(data){
                return JSON.stringify(data,{allowDots:true})
            }],
            headers: {
                'Content-Type': 'application/json'
            }
        })
    },
    add(url,id){
        const param = new URLSearchParams();
        param.append("bookId", id);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    sub(url,id){
        const param = new URLSearchParams();
        param.append("bookId", id);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    getUser(url, name) {
        return axios({
            method: 'get',
            url: `${url+ "?username=" + name}`
        })
    },
    getUserByIds(url, ids) {
        return axios({
            method: 'get',
            url: `${url}`,
            params: {
                ids: ids,
            }
        })
    },
    updateUser(url, user) {
        return axios({
            method: 'post',
            url: `${url}`,
            data: user,
            transformRequest: [function (data) {
                return JSON.stringify(data, { allowDots: true })
            }],
            headers: {
                'Content-Type': 'application/json'
            }
        })
    },
    addOrder(url, totalPrice, pay) {
        const param = new URLSearchParams();
        param.append("totalPrice", totalPrice);
        param.append("pay", pay);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    updateOrder(url, orderId, status) {
        const param = new URLSearchParams();
        param.append("orderId", orderId);
        param.append("status", status);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    updateOrderDetail(url, orderId, bookId, status) {
        const param = new URLSearchParams();
        param.append("orderId", orderId); 
        param.append("bookId", bookId);
        param.append("status", status);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    addOrderDetail(url, data) {
        const param = new URLSearchParams();
        param.append("orderId", data.orderId);
        param.append("status", data.status);
        param.append("ids", data.ids);
        param.append("nums", data.nums);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    getByStatus(url, status) {
        return axios({
            method: 'get',
            url: `${url + "?status="+status}`
        })
    },
    getByUserId(url) {
        return axios({
            method: 'get',
            url: `${url}`
        })
    },
    getAll(url, pageNum, pageSize) {
        return axios({
            method: 'get',
            url: `${url}`,
            params: {
                pageNum: pageNum,
                pageSize: pageSize
            }
        })
    },
    getDetail(url, orderIds, status) {
        return axios({
            method: 'get',
            url: `${url}`,
            params: {
                orderIds: orderIds,
                status: status
            }
        })
    },
    getDetailByOrderId(url, orderId) {
        return axios({
            method: 'get',
            url: `${url}`,
            params: {
                orderId: orderId,
            }
        })
    },
    getCommentByBookId(url, bookId) {
        return axios({
            method: 'get',
            url: `${url}`,
            params: {
                bookId: bookId,
            }
        })
    },
    getAllComment(url) {
        return axios({
            method: 'get',
            url: `${url}`
        })
    },
    addComment(url, bookId, rate, content){
        const param = new URLSearchParams();
        param.append("bookId", bookId);
        param.append("rate", rate);
        param.append("content", content);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    appendComment(url, bookId, append){
        const param = new URLSearchParams();
        param.append("bookId", bookId);
        param.append("appendContent", append);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    getBooksByUserId(url, userId) {
        return axios({
            method: 'get',
            url: `${url+ "?userId=" + userId}`
        })
    },
    updateByIds(url, ids) {
        const param = new URLSearchParams();
        param.append("ids", ids);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    deleteByIds(url, ids) {
        const param = new URLSearchParams();
        param.append("ids", ids);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    deleteOrderDetail(url, orderIds, bookIds) {
        const param = new URLSearchParams();
        param.append("orderIds", orderIds);
        param.append("bookIds", bookIds);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    cancelOrder(url, orderId, reason) {
        const param = new URLSearchParams();
        param.append("orderId", orderId);
        param.append("reason", reason);
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    uploadImg(url, img) {
        const param = new FormData()
        param.append("img", img)
        return axios({
            method:'post',
            url:`${url}`,
            data:param,
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    }

}

